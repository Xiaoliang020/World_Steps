package com.travel.service;

import com.travel.dto.CommentDTO;
import com.travel.dto.PostDTO;
import com.travel.dto.PostPageQueryDTO;
import com.travel.result.PageResult;
import com.travel.vo.PostVO;

public interface PostService {

    /**
     * Save a post
     * @param postDTO
     */
    void save(PostDTO postDTO);

    /**
     * Post page query
     * @param postPageQueryDTO
     * @return
     */
    PageResult pageQuery(PostPageQueryDTO postPageQueryDTO);

    /**
     * Get post by id
     * @param postId
     * @return
     */
    PostVO getById(Long postId);

    /**
     * Add a new comment reply to post
     * @param commentDTO
     */
    void addReply(CommentDTO commentDTO);
}
