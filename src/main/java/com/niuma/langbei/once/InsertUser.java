package com.niuma.langbei.once;

import com.niuma.langbei.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author niumazlb
 * @create 2022-07-22 11:36
 */
@Component
public class InsertUser {

    @Resource
    private UserService userService;

    //    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {

    }

}
