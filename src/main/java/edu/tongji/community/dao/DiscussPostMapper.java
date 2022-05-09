package edu.tongji.community.dao;

import edu.tongji.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    // 首页查询功能
    List<DiscussPost> selectDiscussPosts(int userId, int offset,int limit);
    // @Param注解用于给参数取别名，如果只有一个参数，并且再<if>动态sql中使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

}
