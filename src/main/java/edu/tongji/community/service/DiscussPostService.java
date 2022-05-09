package edu.tongji.community.service;

import edu.tongji.community.dao.DiscussPostMapper;
import edu.tongji.community.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService {
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);

    public int findDiscussPostRows(int userId);
}
