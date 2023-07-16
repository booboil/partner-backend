package com.niuma.langbei;

import com.niuma.langbei.service.chatService.byNetty.ChatNettyWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author dogbin
 */
@SpringBootApplication
@MapperScan("com.niuma.langbei.mapper")
@EnableScheduling
//@EnableElasticsearchRepositories(basePackages="com.niuma.langbei.model.repository")
public class LangBeiApplication  implements CommandLineRunner {
    @Resource
    ChatNettyWebSocketServer ChatNettyWebSocketServer;
    public static void main(String[] args) {
        SpringApplication.run(LangBeiApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        //启动聊天室服务 打开/templates/index.html
//        ChatNettyWebSocketServer.run();
    }

}
