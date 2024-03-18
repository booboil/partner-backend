package com.booboil.partner;

import com.booboil.partner.service.chatService.byNetty.ChatNettyWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author dogbin
 */
@SpringBootApplication
@MapperScan("com.booboil.partner.mapper")
@EnableScheduling
//@EnableElasticsearchRepositories(basePackages="com.booboil.partner.model.repository")
public class PartnerApplication implements CommandLineRunner {
    @Resource
    ChatNettyWebSocketServer ChatNettyWebSocketServer;
    public static void main(String[] args) {
        SpringApplication.run(PartnerApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        //启动聊天室服务 打开/templates/index.html
//        ChatNettyWebSocketServer.run();
    }

}
