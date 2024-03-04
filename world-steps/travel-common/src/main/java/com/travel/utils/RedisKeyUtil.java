package com.travel.utils;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    private static final String PREFIX_USER_LIKE = "like:user";

    private static final String PREFIX_FOLLOWER = "follower";

    private static final String PREFIX_FOLLOWEE = "followee";

    private static final String PREFIX_USER = "user";

    // Some entity's likes
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + entityId;
    }

    // A user's like
    // like:user:userId -> int
    public static String getUserLikeKey(Long userId) {
        return PREFIX_USER_LIKE + SPLIT +userId;
    }

    // A user's following
    // followee:userId:entityType -> zset(entityId, now)
    public static String getFolloweeKey(Long userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // A user's followers
    // follower:entityType:entityId -> zset(userId, now)
    public static String getFollowerKey(int entityType, Long entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // User
    public static String getUserKey(Long userId) {
        return PREFIX_USER + SPLIT + userId;
    }
}
