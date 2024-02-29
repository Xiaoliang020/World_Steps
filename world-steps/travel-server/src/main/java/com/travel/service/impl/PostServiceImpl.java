package com.travel.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.travel.dto.PostDTO;
import com.travel.dto.PostPageQueryDTO;
import com.travel.entity.Post;
import com.travel.mapper.PostMapper;
import com.travel.result.PageResult;
import com.travel.service.PostService;
import com.travel.vo.PostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

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
}
