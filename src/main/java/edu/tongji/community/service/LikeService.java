package edu.tongji.community.service;

import io.lettuce.core.api.push.PushListener;

public interface LikeService {
    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId);

    public long findEntityLikeCount(int entityType, int entityId);

    public int findEntityLikeStatus(int userId, int entityType, int entityId);

    // 查询某个用户获得的赞
    public int findUserLikeCount(int userId);
}
