//package com.booboil.partner.service.impl;
//
//import com.booboil.partner.model.domain.User;
//import com.booboil.partner.model.repository.UserRepository;
//import com.booboil.partner.model.vo.UserVo;
//import com.booboil.partner.service.UserSearchService;
//import com.booboil.partner.service.UserService;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.BeanUtils;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
///**
// * 用户搜索服务
// * @author booboil
// */
//@Service
//public class UserSearchServiceImpl implements UserSearchService {
//
//    @Resource
//    private UserRepository elasticRepository;
//    @Resource
//    private ElasticsearchRestTemplate elasticsearchTemplate;
//
//    @Resource
//    private UserService userService;
//
//    @Override
//    public List<UserVo> findByUserAccount(String userAccount) {
//       return elasticRepository.findByUserAccount(userAccount);
//    }
//
//    @Override
//    public List<UserVo> findByAll(String all) {
//        if(StringUtils.isBlank(all)){
//           return this.findAll();
//        }
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchQuery("all",all))
//                        .build();
//        SearchHits<UserVo> search = elasticsearchTemplate.search(query, UserVo.class);
//        List<SearchHit<UserVo>> searchHits = search.getSearchHits();
//        List<UserVo> userVos = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
//        return userVos;
//    }
//
//    @Override
//    public void save(UserVo docBean) {
//        elasticRepository.save(docBean);
//    }
//
//    @Override
//    public void saveAll(List<UserVo> list) {
//        elasticRepository.saveAll(list);
//    }
//
//    @Override
//    public List<UserVo> findAll() {
//        Iterator<UserVo> iterator = elasticRepository.findAll().iterator();
//        List<UserVo> userVos = new ArrayList<>();
//        while (iterator.hasNext()){
//            userVos.add(iterator.next());
//        }
//        return userVos;
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        elasticRepository.deleteById(id);
//    }
//
//    @Override
//    public void insertUser(Long id) {
//        User user = userService.getById(id);
//        UserVo userVo = new UserVo();
//        BeanUtils.copyProperties(user,userVo);
//        this.save(userVo);
//    }
//}
