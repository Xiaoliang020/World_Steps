package com.travel.service;

import com.travel.dto.LikeDTO;

public interface LikeService {

    /**
     * Add a like to an entity
     * @param likeDTO
     */
    void like(LikeDTO likeDTO);

    /**
     * Find the count of like
     * @param likeDTO
     * @return
     */
    long findEntityLikeCount(LikeDTO likeDTO);

    /**
     * Find the like status
     * @return
     */
    int findLikeStatus(LikeDTO likeDTO);

    /**
     * Find a user's like count
     * @param userId
     * @return
     */
    int findUserLikeCount(Long userId);
}
