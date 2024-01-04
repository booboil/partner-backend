//package com.booboil.partner.model.repository;
//
//import com.booboil.partner.model.vo.UserVo;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
///**
// *
// * @author niuma
// * @create 2023-02-21 21:07
// */
//@Repository
//public interface UserRepository extends ElasticsearchRepository<UserVo,Long> {
//    /**
//     * 满足规定格式，可以自定义查询方法
//     *  findBy{Titlte}And{contnet}(String title,String contnet)
//     * @param userAccount
//     * @return
//     */
//    List<UserVo> findByUserAccount(String userAccount);
//
//    /**
//     * 根据组合字段查询
//     * @param all
//     * @return
//     */
//    List<UserVo> findByAll(String all);
//}
