package edu.tongji.community.service;

import java.util.List;
import java.util.Map;

public interface FollowService {


    public void follow(int userId, int entityType, int entityId);

    public void unfollow(int userId, int entityType, int entityId);

    long findFolloweeCount(int userId, int entityTypeUser);

    public long findFollowerCount(int entityType, int entityId);

    public boolean hasFollowed(int userId, int entityType, int entityId);

    // 查询某用户关注的人
    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit);

    // 查询某用户的粉丝
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit);
}
