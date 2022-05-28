package edu.tongji.community.service.impl;

import edu.tongji.community.dao.CommentMapper;
import edu.tongji.community.entity.Comment;
import edu.tongji.community.service.CommentService;
import edu.tongji.community.service.DiscussPostService;
import edu.tongji.community.util.CommunityConstant;
import edu.tongji.community.util.SensitiveFilter;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    public DiscussPostService discussPostService;

    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
            // 添加评论
            comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
            comment.setContent(sensitiveFilter.filter(comment.getContent()));
            int rows = commentMapper.insertComment(comment);

            // 更新帖子的评论数量
            if (comment.getEntityType() == ENTITY_TYPE_POST) {
                int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
                discussPostService.updateCommentCount(comment.getEntityId(), count);
            }
        return rows;
    }

    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }
}
