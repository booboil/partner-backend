package com.booboil.partner.model.domain;

import lombok.Data;

/**
 * @author booboil
 */
@Data
public class ImUser {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 用户昵称
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
}
