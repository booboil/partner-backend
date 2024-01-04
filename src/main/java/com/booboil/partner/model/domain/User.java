package com.booboil.partner.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private long id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 标签列表
     */
    private String tags;
    /**
     * 电话
     */
    private String phone;
    /**
     * 联系方式
     */
    private String contactInfo;
    /**
     * 个人简介
     */
    private String profile;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;
    /**
     * 星球编号
     */
    private String planetCode;

}