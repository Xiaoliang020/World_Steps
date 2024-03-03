package com.travel.service.impl;

import com.travel.dto.LikeDTO;
import com.travel.service.LikeService;
import com.travel.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Add a like to an entity
     * @param likeDTO
     */
    public void like(LikeDTO likeDTO) {
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(likeDTO.getEntityType(), likeDTO.getEntityId());
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, likeDTO.getUserId());
//        if (isMember) {
//            redisTemplate.opsForSet().remove(entityLikeKey, likeDTO.getUserId());
//        } else {
//            redisTemplate.opsForSet().add(entityLikeKey, likeDTO.getUserId());
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(likeDTO.getEntityType(), likeDTO.getEntityId());
                String userLikeKey = RedisKeyUtil.getUserLikeKey(likeDTO.getEntityUserId());

                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, likeDTO.getUserId());

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, likeDTO.getUserId());
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, likeDTO.getUserId());
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    /**
     * Find the count of like
     * @param likeDTO
     * @return
     */
    public long findEntityLikeCount(LikeDTO likeDTO) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(likeDTO.getEntityType(), likeDTO.getEntityId());
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * Find the like status
     * @return
     */
    public int findLikeStatus(LikeDTO likeDTO) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(likeDTO.getEntityType(), likeDTO.getEntityId());
        return redisTemplate.opsForSet().isMember(entityLikeKey, likeDTO.getUserId()) ? 1 : 0;
    }

    /**
     * Find a user's like count
     * @param userId
     * @return
     */
    public int findUserLikeCount(Long userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}
