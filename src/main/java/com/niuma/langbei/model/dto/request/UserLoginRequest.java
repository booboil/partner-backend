package com.niuma.langbei.model.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author niumazlb
 * @create 2022-03-19 17:05
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -8310782895897361376L;
    private String userAccount;
    private String userPassword;

}
