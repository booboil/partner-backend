package com.booboil.partner.once;

import com.booboil.partner.mapper.UserMapper;
import com.booboil.partner.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @author booboil
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    //    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)

    public static void main(String[] args) {
        new InsertUsers().doInsertUsers();
    }

    /**
     * 批量插入用户
     */
    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户");
            user.setUserAccount("fakeyupi");
            user.setAvatarUrl("https://tse2-mm.cn.bing.net/th/id/OIP-C.B8QqjXNW9xWGnsilmfuFJwAAAA?pid=ImgDet&rs=1");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setTags("[]");
            user.setPhone("18676085990");
            user.setProfile("123@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("1111111");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
