package com.niuma.langbei.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author niumazlb
 * @create 2022-08-30 21:24
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 6921116057127994572L;

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
     * 过期时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date expireTime;

    /**
     * 最大人数
     */
    private Integer maxNum;


    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}
