package edu.tongji.community.dao;

import edu.tongji.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 根据当前用户的ID查询所有的会话列表
    // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 根据当前的用户id查询会话的总数
    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 根据单个会话的id查询私信列表List<message>
    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 根据当前会话的id查询私信列表中的消息数
    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 根据当前用户的id和会话的id查询该次会话的消息数，toId=userID
    // 查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息的状态
    int updateStatus(List<Integer> ids, int status);

    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题所包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某个主题所包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
