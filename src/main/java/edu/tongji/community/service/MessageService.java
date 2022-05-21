package edu.tongji.community.service;

import edu.tongji.community.entity.Message;

import java.util.List;

public interface MessageService {

    public List<Message>findConversations(int userId, int offset, int limit);

    public int findConversationCount(int userId);

    public List<Message> findLetters(String conversationId, int offset, int limit);

    public int findLetterCount(String conversationId);

    public int addMessage(Message message);

    public int findLetterUnreadCount(int userId, String conversationId);

    public int readMessage(List<Integer> ids);
}
