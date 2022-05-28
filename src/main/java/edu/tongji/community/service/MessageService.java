package edu.tongji.community.service;

import edu.tongji.community.entity.Message;
import org.springframework.data.repository.config.ResourceReaderRepositoryPopulatorBeanDefinitionParser;

import java.util.List;

public interface MessageService {

    public List<Message>findConversations(int userId, int offset, int limit);

    public int findConversationCount(int userId);

    public List<Message> findLetters(String conversationId, int offset, int limit);

    public int findLetterCount(String conversationId);

    public int addMessage(Message message);

    public int findLetterUnreadCount(int userId, String conversationId);

    public int readMessage(List<Integer> ids);

    public Message findLatestNotice(int userId, String topic);

    public int findNoticeCount(int userId, String topic);

    public int findNoticeUnreadCount(int userId, String topic);

    public List<Message> findNotices(int userId, String topic, int offset, int limit);
}
