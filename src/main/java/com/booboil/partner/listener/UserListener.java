//package com.booboil.partner.listener;
//
//import com.booboil.partner.constant.MqConstants;
//import com.booboil.partner.service.UserSearchService;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author booboil
// */
//@Component
//public class UserListener {
//    @Resource
//    UserSearchService userSearchService;
//
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    key = MqConstants.USER_INSERT_KEY,
//                    exchange = @Exchange(type = "topic",name = MqConstants.USER_EXCHANGE)
//            )
//    })
//    public void userInsertListener(Long id){
//        userSearchService.insertUser(id);
//    }
//
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    key = MqConstants.USER_DELETE_KEY,
//                    exchange = @Exchange(type = "topic",name = MqConstants.USER_EXCHANGE)
//            )
//    })
//    public void userDeleteListener(Long id){
//        userSearchService.deleteById(id);
//    }
//}
