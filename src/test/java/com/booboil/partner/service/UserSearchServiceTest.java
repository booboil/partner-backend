//package com.booboil.partner.service;
//
//import com.booboil.partner.model.vo.UserVo;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//
//import javax.annotation.Resource;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @author booboil
// */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserSearchServiceTest {
////    @Resource
////    UserSearchService userSearchService;
//    @Resource
//    UserService userService;
//
//
//    /**
//     * 创建索引库以及 mapping
//     */
//    @Test
//    void saveAll() {
//        //从数据库读取数据并加入list
//        List<UserVo> list = userService.list().stream().map(user -> {
//            UserVo userVo = new UserVo();
//            BeanUtils.copyProperties(user, userVo);
//            return userVo;
//        }).collect(Collectors.toList());
//        //批量加入文档
//        userSearchService.saveAll(list);
//    }
//
//    @Test
//    void findAll() {
//        List<UserVo> all = userSearchService.findAll();
//    }
//
//    @Test
//    void findByUserAccount() {
//        List<UserVo> userVos = userSearchService.findByUserAccount("4");
//        System.out.println(userVos);
//    }
//
//    @Test
//    void findByAll() {
//        List<UserVo> userVos = userSearchService.findByAll("小孩");
//        userVos.forEach(System.out::println);
//    }
//
//    @Resource
//    ElasticsearchRestTemplate template;
//
//    @Test
//    void testElasticsearchRestTemplate(){
////        //创建索引库
////        template.indexOps(UserVo.class).create();//从对象中创建
////        template.indexOps(IndexCoordinates.of("partner")).create();//指定名称创建
////        //创建mapping
////        Document mapping = template.indexOps(UserVo.class).createMapping();
////        template.indexOps(UserVo.class).putMapping(mapping);
//
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchQuery("userAccount","4")) //查询条件
//                .withHighlightBuilder(new HighlightBuilder().field("userAccount").preTags("<em>").postTags("</em>")) //高亮设置前后缀
//                .build();
//        SearchHits<UserVo> searchHits = template.search(query, UserVo.class); //执行查询
//        long totalHits = searchHits.getTotalHits(); //获取查询到的条数
//        System.out.println("totalHits = " + totalHits);
//        List<SearchHit<UserVo>> searchHits1 = searchHits.getSearchHits();
//        searchHits1.forEach( e->{
//            UserVo content = e.getContent();
//            System.out.println("content = " + content);
//            Map<String, List<String>> highlightFields = e.getHighlightFields();
//            List<String> highlightUserAccount = highlightFields.get("userAccount");
//            System.out.println(highlightUserAccount);
//        });
//
//    }
//
//    @Test
//    void save() {
//    }
//
//    @Test
//    void deleteById() {
//        userSearchService.deleteById(6110464L);
//    }
//}