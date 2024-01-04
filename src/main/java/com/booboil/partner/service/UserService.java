package com.booboil.partner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.booboil.partner.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author niumazlb
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2022-03-12 21:21:05
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode, String avatarUrl);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取一个脱敏的用户信息
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     *
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    int updateUser(User user, User loginUser);

    /**
     * 是否为管理员
     *
     * @param request
     * @return 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return 是否为管理员
     */
    boolean isAdmin(User user);

    List<User> matchUsers(long num, User loginUser);

}
