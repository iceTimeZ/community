package edu.tongji.community.controller;


import edu.tongji.community.entity.Comment;
import edu.tongji.community.entity.DiscussPost;
import edu.tongji.community.entity.Event;
import edu.tongji.community.event.EventProducer;
import edu.tongji.community.service.CommentService;
import edu.tongji.community.service.DiscussPostService;
import edu.tongji.community.util.CommunityConstant;
import edu.tongji.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    // 发表评论时要触发评论事件，系统会给帖子或者评论的作者发送系统通知
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

/*        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setData("postId", discussPostId);
        // 评论的对象时帖子就获取帖子的作者id
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
            // 评论对象时评论，则获取评论的作者id，与前面调用的service不同
        }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }*/
        // 生产者发送消息
        eventProducer.fireEvent(event);

        // 消息发完后就进行了页面跳转，而消息队列处理消息可能会有延迟，两者是并发的(异步的)，消息处理由另一个线程处理
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
