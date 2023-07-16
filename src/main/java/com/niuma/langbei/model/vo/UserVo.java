package com.niuma.langbei.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
//import sun.jvm.hotspot.oops.FieldType;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

/**
 * @author niumazlb
 * @create 2022-08-30 19:41
 */
@Data
//@Document(indexName = "langbei")
//@Setting(shards = 1,replicas = 1)
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @Id
    private long id;
    /**
     * 用户昵称
     */
//    @Field(name = "username",copyTo = "all",type = FieldType.Text,searchAnalyzer="ik_max_word",analyzer="ik_max_word")
    private String username;
    /**
     * 用户账号
     */
//    @Field(name = "userAccount",type = FieldType.Text,searchAnalyzer="ik_max_word",analyzer="ik_max_word",copyTo = "all")
    private String userAccount;
    /**
     * 用户头像
     */
//    @Field(name = "avatarUrl")
    private String avatarUrl;
    /**
     * 性别
     */
//    @Field(name = "gender")
    private Integer gender;
    /**
     * 标签列表
     */
//    @Field(name = "tags",copyTo = "all",type = FieldType.Text,searchAnalyzer="ik_max_word",analyzer="ik_max_word")
    private String tags;
    /**
     * 电话
     */
//    @Field(name = "phone")
    private String phone;
    /**
     * 联系方式
     */
//    @Field(name = "contactInfo")
    private String contactInfo;
    /**
     * 个人简介
     */
//    @Field(name = "profile",copyTo = "all",type = FieldType.Text,searchAnalyzer="ik_max_word",analyzer="ik_max_word")
    private String profile;
    /**
     * 用户状态
     */
//    @Field(name = "userStatus")
    private Integer userStatus;
    /**
     * 创建时间
     */
//    @Field(name = "createTime")
    private Date createTime;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
//    @Field(name = "userRole")
    private Integer userRole;
    /**
     * 编号
     */
//    @Field(name = "planetCode")
    private String planetCode;

//    @Field(name = "all",type = FieldType.Text,searchAnalyzer="ik_max_word",analyzer="ik_max_word")
//    private String all;
}
