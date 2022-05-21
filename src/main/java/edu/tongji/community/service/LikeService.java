package edu.tongji.community.service;

public interface LikeService {
    // 点赞
    public void like(int userId, int entityType, int entityId);

    public long findEntityLikeCount(int entityType, int entityId);

    public int findEntityLikeStatus(int userId, int entityType, int entityId);
}
