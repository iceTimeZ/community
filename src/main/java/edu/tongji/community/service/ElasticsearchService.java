package edu.tongji.community.service;


import edu.tongji.community.dao.elasticsearch.DiscussPostRepository;
import edu.tongji.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void saveDiscussPost(DiscussPost post) {
        discussPostRepository.save(post);
    }

    public void deleteDiscussPost(int id) {
        discussPostRepository.deleteById(id);
    }

    SearchHits<DiscussPost> searchHits;

    // 将查询结果的总条数返回给分页对象，设置行数
    public Long getRows(){
        return searchHits.getTotalHits();
    }

    public List<DiscussPost> searchDiscussPost(String keyword,int current, int limit) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // 查询
        searchHits = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);

        //从searchHits取相关数据，取命中的总记录条数
        long totalHits = searchHits.getTotalHits();
        System.out.println(totalHits);
        // 得到查询结果得集合

        List<SearchHit<DiscussPost>> list = searchHits.getSearchHits();
        System.out.println("查询结果集合中SearchHit<DiscussPost>对象的个数" + list.size());
        // 设置一个最后需要返回的实体类集合,里面帖子的标题和内容已经加了高亮标签
        List<DiscussPost> discussPosts = new ArrayList<>();
        // 对集合进行遍历，将加亮标签的高亮标题和内容存到帖子中
        for (SearchHit<DiscussPost> searchHit : list) {
            // 取原生文档数据
            DiscussPost discussPost = searchHit.getContent();

            // 取高亮对象，是"互联网寒冬"匹配到的句子，会给句子内的互联网和寒冬加上<em>标签
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();

            // 取高亮对象放到discussPost里去 这样就将discussPost和高亮结合输出了
            // 高亮内容可能有多个，如title中可能有"互联网"，有"寒冬"，或者什么都没有，我们只取第一个匹配到的高亮内容

//            String title = highlightFields.get("title").get(0);
//            String content = highlightFields.get("content").get(0);

            // 如果标题里没有关键字，内容中有，则标题为null，需要判断并补全标题，内容无关键字同理
            // 将高亮的内容填充到content中,如果没匹配到就填原来的内容，如果有匹配到的高亮内容，就将加了标签的内容填到帖子中
            discussPost.setTitle(highlightFields.get("title") == null ? discussPost.getTitle() : highlightFields.get("title").get(0));
            discussPost.setContent(highlightFields.get("content") == null ? discussPost.getContent() : highlightFields.get("content").get(0));
            // 放到实体类中
            discussPosts.add(discussPost);
        }
        System.out.println(discussPosts.size());
        return discussPosts;
    }
}
