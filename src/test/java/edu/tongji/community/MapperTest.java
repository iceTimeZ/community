package edu.tongji.community;

import edu.tongji.community.dao.LoginTicketMapper;
import edu.tongji.community.dao.MessageMapper;
import edu.tongji.community.entity.LoginTicket;
import edu.tongji.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void insertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(111);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60));
        loginTicketMapper.insertLoginTicket(loginTicket);

    }

    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testSelectLetters(){
        // 根据当前用户的ID查询所有的会话列表
        List<edu.tongji.community.entity.Message> list = messageMapper.selectConversations(111,0,20);
        for(Message message : list){
            System.out.println(message);
        }
        // 根据当前的用户id查询会话的总数
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        // 根据单个会话的id查询私信列表List<message>
        list = messageMapper.selectLetters("111_112",0,10);
        for(Message message : list){
            System.out.println(message);
        }

        // 根据当前会话的id查询私信列表中的消息数
        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        // 根据当前用户的id和会话的id查询该次会话的消息数，toId=userID
        count = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count);
    }
}
