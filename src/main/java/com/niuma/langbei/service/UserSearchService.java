//package com.niuma.langbei.service;
//
//import com.niuma.langbei.model.vo.UserVo;
//
//import java.util.Iterator;
//import java.util.List;
//
//
///**
// * @author niumazlb
// */
//public interface UserSearchService {
//
//    /**
//     * 通过用户账号搜索用户
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
//
//    /**
//     * 保存用户
//     * @param docBean
//     */
//    void save(UserVo docBean);
//
//    /**
//     * 批量保存用户
//     * @param list
//     */
//    void saveAll(List<UserVo> list);
//
//    /**
//     * 查询所有用户
//     * @return
//     */
//    List<UserVo> findAll();
//
//    /**
//     * 删除数据
//     * @param id
//     */
//    void deleteById(Long id);
//
//    /**
//     * 根据id,插入/修改用户
//     * @param id
//     */
//    void insertUser(Long id);
//}
