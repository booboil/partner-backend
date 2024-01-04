package com.booboil.partner.once;

import com.booboil.partner.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author booboil
 */
@Component
public class InsertUser {

    @Resource
    private UserService userService;

    //    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {

    }

}
