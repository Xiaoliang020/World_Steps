package com.travel.service.impl;

import com.travel.constant.EntityTypeConstant;
import com.travel.context.BaseContext;
import com.travel.dto.FollowDTO;
import com.travel.dto.FollowPageQueryDTO;
import com.travel.entity.User;
import com.travel.mapper.UserMapper;
import com.travel.result.PageResult;
import com.travel.service.FollowService;
import com.travel.utils.RedisKeyUtil;
import com.travel.vo.UserFollowVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

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

    /**
     * Page query followings
     * @param followPageQueryDTO
     * @return
     */
    public PageResult pageFollowings(FollowPageQueryDTO followPageQueryDTO) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(followPageQueryDTO.getUserId(), EntityTypeConstant.ENTITY_TYPE_USER);
        List<UserFollowVO> pageResult = getPageResult(followPageQueryDTO, followeeKey);
        if (pageResult == null) {
            return new PageResult();
        }
        Long total = findFollowingCount(followPageQueryDTO.getUserId(), EntityTypeConstant.ENTITY_TYPE_USER);
        return new PageResult(total, pageResult);
    }

    /**
     * Page query followers
     * @param followPageQueryDTO
     * @return
     */
    public PageResult pageFollowers(FollowPageQueryDTO followPageQueryDTO) {
        String followerKey = RedisKeyUtil.getFollowerKey(EntityTypeConstant.ENTITY_TYPE_USER, followPageQueryDTO.getUserId());
        List<UserFollowVO> pageResult = getPageResult(followPageQueryDTO, followerKey);
        if (pageResult == null) {
            return new PageResult();
        }
        Long total = findFollowerCount(EntityTypeConstant.ENTITY_TYPE_USER, followPageQueryDTO.getUserId());
        return new PageResult(total, pageResult);
    }

    private List<UserFollowVO> getPageResult(FollowPageQueryDTO followPageQueryDTO, String followerKey) {
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, followPageQueryDTO.getPage() - 1,
                followPageQueryDTO.getPage() - 1 + followPageQueryDTO.getPageSize() - 1);

        if (targetIds == null) {
            return null;
        }

        List<UserFollowVO> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            System.out.println("Get user: " + targetId);
            User user = userMapper.getById(Long.valueOf(targetId));
            UserFollowVO userFollowVO = new UserFollowVO();
            BeanUtils.copyProperties(user, userFollowVO);
            userFollowVO.setHasFollowed(hasFollowed(BaseContext.getCurrentId(), EntityTypeConstant.ENTITY_TYPE_USER, user.getId()));
            list.add(userFollowVO);
        }

        return list;
    }
}
