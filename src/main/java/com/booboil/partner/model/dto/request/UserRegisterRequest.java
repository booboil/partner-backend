package com.booboil.partner.model.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author niumazlb
 * @create 2022-03-19 17:05
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -8310782895897361376L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
    private String avatarUrl;

}
