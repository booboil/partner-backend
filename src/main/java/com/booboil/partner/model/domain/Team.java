package com.booboil.partner.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍
 *
 * @TableName team
 */
@TableName(value = "team")
@Data
public class Team implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 队伍名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 公告
     */
    private String announce;
    /**
     * 描述
     */
    private String avatarUrl;
    /**
     * 最大人数
     */
    private Integer maxNum;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}