package edu.tongji.community;

import edu.tongji.community.dao.DiscussPostMapper;
import edu.tongji.community.dao.UserMapper;
import edu.tongji.community.entity.DiscussPost;
import edu.tongji.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = new User();
        user1.setUsername("计算机网络");
        user1.setPassword("123123");
        user1.setSalt("abad");
        user1.setEmail("516345145@qq.ocm");
        user1.setHeaderUrl("http://www.nowcoder.com/101.png");
        user1.setCreateTime(new Date());

        int rows = userMapper.insertUser(user1);
        System.out.println(rows);
        System.out.println(user1.getId());

    }
    @Test
    public void testSelectPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149,0,10);
        for(DiscussPost post : list){
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
