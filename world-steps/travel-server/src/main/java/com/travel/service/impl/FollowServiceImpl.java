package com.travel.service.impl;

import com.travel.dto.FollowDTO;
import com.travel.service.FollowService;
import com.travel.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Follow an entity
     * @param followDTO
     */
    public void follow(FollowDTO followDTO) {
        Long userId = followDTO.getUserId();
        Long entityId = followDTO.getEntityId();
        int entityType = followDTO.getEntityType();
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    /**
     * Unfollow an entity
     * @param followDTO
     */
    public void unfollow(FollowDTO followDTO) {
        Long userId = followDTO.getUserId();
        Long entityId = followDTO.getEntityId();
        int entityType = followDTO.getEntityType();
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    /**
     * Find following count
     * @param userId
     * @param entityType
     * @return
     */
    public long findFollowingCount(Long userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    /**
     * Find an entity's follower count
     * @param entityType
     * @param entityId
     * @return
     */
    public long findFollowerCount(int entityType, Long entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    /**
     * Find if user has followed the entity
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean hasFollowed(Long userId, int entityType, Long entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }
}
