package com.booboil.partner.model.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author booboil
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -8310782895897361376L;
    private String userAccount;
    private String userPassword;

}
