package com.travel.service;

import com.travel.dto.FollowDTO;

public interface FollowService {

    /**
     * Follow an entity
     * @param followDTO
     */
    void follow(FollowDTO followDTO);

    /**
     * Unfollow an entity
     * @param followDTO
     */
    void unfollow(FollowDTO followDTO);

    /**
     * Find following count
     * @param userId
     * @param entityType
     * @return
     */
    long findFollowingCount(Long userId, int entityType);

    /**
     * Find an entity's follower count
     * @param entityType
     * @param entityId
     * @return
     */
    long findFollowerCount(int entityType, Long entityId);

    /**
     * Find if user has followed the entity
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean hasFollowed(Long userId, int entityType, Long entityId);
}
