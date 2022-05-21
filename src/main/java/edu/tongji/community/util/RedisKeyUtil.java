package edu.tongji.community.util;

// 管理Key
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LKE = "like:entity";

    // 某个实体的赞
    // key格式 like:entity:entityType:entityId -> 存的值为集合 set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){

        return PREFIX_ENTITY_LKE + SPLIT + entityType + SPLIT + entityId;
    }
}
