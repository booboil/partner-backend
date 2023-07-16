package com.niuma.langbei.service.chatService;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.niuma.langbei.config.HttpSessionConfigurator;
import com.niuma.langbei.constant.ChatConstant;
import com.niuma.langbei.constant.UserConstant;
import com.niuma.langbei.model.domain.Im;
import com.niuma.langbei.model.domain.ImUser;
import com.niuma.langbei.model.domain.User;
import com.niuma.langbei.model.vo.ImMessageVo;
import com.niuma.langbei.service.ImService;
import com.niuma.langbei.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket服务
 *
 * @author niuma
 * @create 2023-02-13 21:42
 */
@ServerEndpoint(value = "/im", configurator = HttpSessionConfigurator.class)
@Component
public class ChatWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketServer.class);
    /**
     * 记录当前在线连接数
     * key - 用户id , value - 用户会话
     */
    public static final Map<Long, Session> sessionMap = new ConcurrentHashMap<>();
    public static final Gson gson = new Gson();

    HttpSession httpSession;

    @Resource
    ImService imService;
    @Resource
    UserService userService;

    private static ImService staticImService;
    private static UserService staticUserService;

    // 程序初始化的时候触发这个方法  赋值
    @PostConstruct
    public void setStaticUser() {
        staticImService = imService;
        staticUserService = userService;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        User loginUser = (User) httpSession.getAttribute(UserConstant.USER_LOGIN_STATE);

        if(loginUser == null){
            return;
        }

        sessionMap.put(loginUser.getId(), session);
        log.info("有新用户加入，uid={}, 当前在线人数为：{}", loginUser.getId(), sessionMap.size());


        Im im = new Im();
        im.setText(gson.toJson(sessionMap.keySet()));
        ImMessageVo imMessageVo = new ImMessageVo();
        imMessageVo.setType(ChatConstant.CHAT_LOGIN_LIST);
        imMessageVo.setIm(im);
        sendAllMessage(gson.toJson(imMessageVo));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        User loginUser = (User) httpSession.getAttribute(UserConstant.USER_LOGIN_STATE);
        sessionMap.remove(loginUser.getId());
        log.info("有一连接关闭，uid={}的用户session, 当前在线人数为：{}", loginUser.getId(), sessionMap.size());

        Im im = new Im();
        im.setText(gson.toJson(sessionMap.keySet()));
        ImMessageVo imMessageVo = new ImMessageVo();
        imMessageVo.setType(ChatConstant.CHAT_LOGIN_LIST);
        imMessageVo.setIm(im);
        // 后台发送消息给所有的客户端
        sendAllMessage(gson.toJson(imMessageVo));
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session fromSession) throws JsonProcessingException {
        try {
            // 处理msg
            // 存储数据库
            Im im = gson.fromJson(message, Im.class);
            log.info("服务端收到用户uid={}的消息:{}", im.getUid(), message);

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",im.getUid());
            long count = staticUserService.count(queryWrapper);
            if(count <= 0){
                return;
            }

            if(StringUtils.isBlank(im.getText())){
                return;
            }

            //消息最大长度为40
            if(im.getText().length()>40){
                return;
            }

            // 存储数据到数据库
            staticImService.save(im);
            ImMessageVo imMessageVo = new ImMessageVo();
            imMessageVo.setType(ChatConstant.CHAT_TYPE);
            imMessageVo.setIm(im);
            this.sendMessage(imMessageVo, message);
            fromSession.getBasicRemote().sendText(message);
            log.info("发送消息：{}", message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给目标用户
     */
    private void sendMessage(ImMessageVo imMessageVo, String message) {
        try {
            Long toId = imMessageVo.getIm().getToId();
            for (Map.Entry<Long, Session> imUserSessionEntry : sessionMap.entrySet()) {
                Long id = imUserSessionEntry.getKey();
                if (id.equals(toId)) {
                    Session session = imUserSessionEntry.getValue();
                    session.getBasicRemote().sendText(message);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
