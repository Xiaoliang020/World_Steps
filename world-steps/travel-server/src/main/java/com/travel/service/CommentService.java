package com.travel.service;

import com.travel.dto.CommentDTO;
import com.travel.dto.CommentPageQueryDTO;
import com.travel.result.PageResult;

public interface CommentService {

    /**
     * Comment page query
     * @param commentPageQueryDTO
     * @return
     */
    PageResult pageQuery(CommentPageQueryDTO commentPageQueryDTO);

    /**
     * Add a reply to comment
     * @param commentDTO
     */
    void add(CommentDTO commentDTO);
}
