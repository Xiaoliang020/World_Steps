package com.travel.service.impl;

import com.travel.dto.LikeDTO;
import com.travel.service.LikeService;
import com.travel.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(likeDTO.getEntityType(), likeDTO.getEntityId());
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, likeDTO.getUserId());
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, likeDTO.getUserId());
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, likeDTO.getUserId());
        }
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
}
