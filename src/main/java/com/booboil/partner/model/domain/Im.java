package com.booboil.partner.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author niumazlb
 * @TableName im
 */
@TableName(value ="im")
@Data
public class Im implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户id
     */
    private Long toId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 消息内容
     */
    private String text;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 图片
     */
    private String img;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}