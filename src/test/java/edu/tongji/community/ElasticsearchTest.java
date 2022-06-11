package edu.tongji.community;

import edu.tongji.community.dao.DiscussPostMapper;
import edu.tongji.community.dao.elasticsearch.DiscussPostRepository;
import edu.tongji.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchTest {

    // DiscussPostRepository最终实现了CrudRepository
    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    //注入 ElasticsearchRestTemplate，是ElasticsearchOperations子类的子类
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    //创建索引并增加映射配置
    @Test
    public void createIndex() {
        //创建索引，系统初始化会自动创建索引
        System.out.println("创建索引");
    }

    @Test
    public void deleteIndex() {
        //创建索引，系统初始化会自动创建索引
        elasticsearchRestTemplate.indexOps(DiscussPost.class).delete();
        System.out.println("删除索引");
    }

    // 添加数据
    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }

    // 添加多个数据
    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));
    }

    // 修改
    @Test
    public void testUpdate() {
        DiscussPost post = discussPostMapper.selectDiscussPostById(231);
        post.setContent("我是新人，使劲灌水。");
        discussPostRepository.save(post);
    }

    // 删除
    @Test
    public void testDelete() {
        discussPostRepository.deleteById(231);
    }

    @Test
    public void testSearchByRepository() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // 查询
        SearchHits<DiscussPost> searchHits = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);

        //从searchHits取相关数据，取命中的总记录条数
        long totalHits = searchHits.getTotalHits();
        System.out.println(totalHits);
        // 得到查询结果得集合
        List<SearchHit<DiscussPost>> list = searchHits.getSearchHits();
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
            String title = highlightFields.get("title").get(0);
            String content = highlightFields.get("content").get(0);

            //将高亮的内容填充到content中,如果没匹配到就填原来的内容，如果有匹配到的高亮内容，就将加了标签的内容填到帖子中
            discussPost.setTitle(highlightFields.get("title") == null ? discussPost.getTitle() : title);
            discussPost.setContent(highlightFields.get("content") == null ? discussPost.getContent() : content);
            //放到实体类中
            discussPosts.add(discussPost);
        }
        //        SearchHits<DiscussPost> hits = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class, IndexCoordinates.of("discusspost"));
//        if(hits.getTotalHits() > 0) {
//            List<SearchHit<DiscussPost>> searchHits = hits.getSearchHits();
//            for (SearchHit<DiscussPost> searchHit : searchHits) {
//                System.out.println(searchHit.getContent());
//                System.out.println(searchHit.getHighlightFields());
//            }
//        }


//        // 得到查询返回的内容
//        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
//
//        for (SearchHit<DiscussPost> searchHit: searchHits) {
//            System.out.println(searchHit);
//            System.out.println(searchHit.getContent());
//        }
//        //设置一个最后需要返回的实体类集合
//        List<DiscussPost> discussPosts = new ArrayList<>();
//        //遍历返回的内容进行处理
//        for(SearchHit<DiscussPost> searchHit:searchHits){
//            //高亮的内容
//            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
//            //将高亮的内容填充到content中
//            searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
//            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));
//            //放到实体类中
//            discussPosts.add(searchHit.getContent());
//        }
    }
}
