package com.travel.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.travel.context.BaseContext;
import com.travel.dto.LikeDTO;
import com.travel.service.CommentService;
import com.travel.dto.CommentDTO;
import com.travel.dto.CommentPageQueryDTO;
import com.travel.entity.Comment;
import com.travel.mapper.CommentMapper;
import com.travel.mapper.UserMapper;
import com.travel.result.PageResult;
import com.travel.service.LikeService;
import com.travel.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeService likeService;

    /**
     * Comment page query
     * @param commentPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CommentPageQueryDTO commentPageQueryDTO) {
        PageHelper.startPage(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize());
        Page<Comment> page = commentMapper.pageQuery(commentPageQueryDTO);
        List<Comment> result = page.getResult();

        List<CommentVO> commentVOList = new ArrayList<>();

        for (Comment comment : result) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);

            Long entityId = comment.getId();
            List<CommentVO> replies = commentMapper.getByEntity(Comment.ENTITY_TYPE_COMMENT, entityId);
            if (replies != null && replies.size() > 0) {
                List<CommentVO> tempReplies = new ArrayList<>();
                for (CommentVO reply : replies) {
                    List<CommentVO> commentList = commentMapper.getByEntity(Comment.ENTITY_TYPE_COMMENT, reply.getId());
                    if (commentList != null && commentList.size() > 0) {
                        tempReplies.addAll(commentList); // 将元素添加到临时集合中，而不是直接添加到replies中
                    }
                }

                LikeDTO likeDTO = new LikeDTO();
                likeDTO.setEntityId(commentVO.getId());
                likeDTO.setEntityType(Comment.ENTITY_TYPE_COMMENT);
                likeDTO.setUserId(BaseContext.getCurrentId());
                commentVO.setLikeCount(likeService.findEntityLikeCount(likeDTO));
                commentVO.setLikeStatus(likeService.findLikeStatus(likeDTO));

                replies.addAll(tempReplies);

                // Get like count and status for replies
                for (CommentVO reply : replies) {
                    likeDTO.setEntityId(reply.getId());
                    if (reply.getTargetId() != null) {
                        reply.setTargetName(userMapper.getUsernameById(reply.getTargetId()));
                    }
                    reply.setLikeCount(likeService.findEntityLikeCount(likeDTO));
                    reply.setLikeStatus(likeService.findLikeStatus(likeDTO));
                }

                commentVO.setReplies(replies);
            }

            commentVOList.add(commentVO);
        }

        return new PageResult(page.getTotal(), commentVOList);
    }

    /**
     * Add a reply to comment
     * @param commentDTO
     */
    public void add(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setEntityId(commentDTO.getCommentId());
        comment.setEntityType(Comment.ENTITY_TYPE_COMMENT);
        comment.setStatus(0);
        comment.setCreateTime(LocalDateTime.now());

        commentMapper.save(comment);
    }
}
