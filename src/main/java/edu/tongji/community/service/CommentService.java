package edu.tongji.community.service;

import edu.tongji.community.entity.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    public int findCommentCount(int entityType, int entityId);

    public int addComment(Comment comment);

    public Comment findCommentById(int id);

}
