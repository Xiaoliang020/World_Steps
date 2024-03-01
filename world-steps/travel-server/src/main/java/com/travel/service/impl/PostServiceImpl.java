package com.travel.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.travel.constant.MessageConstant;
import com.travel.dto.CommentDTO;
import com.travel.dto.PostDTO;
import com.travel.dto.PostPageQueryDTO;
import com.travel.entity.Comment;
import com.travel.entity.Post;
import com.travel.exception.BaseException;
import com.travel.mapper.CommentMapper;
import com.travel.mapper.PostMapper;
import com.travel.result.PageResult;
import com.travel.service.PostService;
import com.travel.vo.PostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * Save a post
     * @param postDTO
     */
    public void save(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        post.setCreateTime(LocalDateTime.now());
        postMapper.insert(post);
    }

    /**
     * Post page query
     * @param postPageQueryDTO
     * @return
     */
    public PageResult pageQuery(PostPageQueryDTO postPageQueryDTO) {
        PageHelper.startPage(postPageQueryDTO.getPage(), postPageQueryDTO.getPageSize());
        Page<PostVO> page = postMapper.pageQuery(postPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Get post by id
     * @param postId
     * @return
     */
    public PostVO getById(Long postId) {
        Post post = postMapper.getById(postId);
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);

        return postVO;
    }

    /**
     * Add a new comment reply to post
     * @param commentDTO
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addReply(CommentDTO commentDTO) {
        Long postId = commentDTO.getPostId();
        Post post = postMapper.getById(postId);
        if (post == null) {
            throw new BaseException(MessageConstant.POST_NOT_FOUND);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setEntityId(postId);
        comment.setEntityType(Comment.ENTITY_TYPE_POST);
        comment.setStatus(0);
        comment.setCreateTime(LocalDateTime.now());

        // Update the count of comment
        postMapper.updateCount(postId, post.getCommentCount() + 1);

        // Insert a new comment
        commentMapper.save(comment);
    }
}