package com.booboil.partner.config;

import com.booboil.partner.common.ErrorCode;
import com.booboil.partner.exception.BusinessException;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author niumazlb
 */

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        //获取httpsession
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if(httpSession == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //将httpsession保存起来
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}