package edu.tongji.community.controller;


import edu.tongji.community.entity.DiscussPost;
import edu.tongji.community.entity.Page;
import edu.tongji.community.service.ElasticsearchService;
import edu.tongji.community.service.LikeService;
import edu.tongji.community.service.UserService;
import edu.tongji.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // /search?keyword=xxx  将关键词放到请求参数中
    @GetMapping("/search")
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子，返回94条数据,实际返回10条
        List<DiscussPost> searchResult = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());

        System.out.println(searchResult.size());
        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null) {
            for (DiscussPost post : searchResult) {
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        // 分页信息
        page.setPath("/search?keyword=" + keyword);
        // 设置分页总条数，需要将返回的Long类型转int数据类型
        page.setRows(elasticsearchService.getRows() == null ? 0 : elasticsearchService.getRows().intValue());

        System.out.println(elasticsearchService.getRows());
        return "/site/search";
    }

}
