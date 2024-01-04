package com.booboil.partner.constant;

/**
 * rabbitMq常量
 * @author booboil
 */
public class MqConstants {
    /**
     * 交换机
     */
    public final static String USER_EXCHANGE = "user.topic";
    /**
     * 监听新增和修改的队列
     */
    public final static String USER_INSERT_QUEUE = "user.insert.queue";
    /**
     * 监听删除的队列
     */
    public final static String USER_DELETE_QUEUE = "user.delete.queue";
    /**
     * 新增或修改的RoutingKey
     */
    public final static String USER_INSERT_KEY = "user.insert";
    /**
     * 删除的RoutingKey
     */
    public final static String USER_DELETE_KEY = "user.delete";
}