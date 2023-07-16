//package com.niuma.langbei.service;
//
//import com.niuma.langbei.model.domain.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//
///**
// * @author niumazlb
// * @create 2022-07-22 11:52
// */
//@SpringBootTest
//public class InsertUsersTest {
//    @Resource
//    private UserService userService;
//    private ExecutorService executorService = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
//
//    /**
//     * 批量插入数据
//     */
//    @Test
//    public void doInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        final int INSERT_NUM = 1000000;
//        List<User> userList = new ArrayList<>(INSERT_NUM);
//        stopWatch.start();
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("假数据");
//            user.setUserAccount("jiashuju");
//            user.setAvatarUrl("https://image-20221207105444134.png");
//            user.setGender(0);
//            user.setUserPassword("12345678");
//            user.setTags("[]");
//            user.setPhone("123");
//            user.setContactInfo("123@qq.com");
//            user.setProfile("ababa");
//            user.setUserStatus(0);
//            user.setUserRole(0);
//            user.setPlanetCode("34535");
//            userList.add(user);
//        }
//        userService.saveBatch(userList, 1000);
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//
//    @Test
//    public void doConcurrencyInsertUsers() {
//        int batchSize = 25000;
//        int j = 0;
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            List<User> userList = new ArrayList<>();
//            while (true) {
//                j++;
//                User user = new User();
//                user.setUsername("假数据");
//                user.setUserAccount("jiashuju");
//                user.setAvatarUrl("https://image-20221207105444134.png");
//                user.setGender(0);
//                user.setUserPassword("12345678");
//                user.setTags("[]");
//                user.setPhone("123");
//                user.setContactInfo("123@qq.com");
//                user.setProfile("ababa");
//                user.setUserStatus(0);
//                user.setUserRole(0);
//                user.setPlanetCode("34535");
//                userList.add(user);
//                if (j % batchSize == 0) {
//                    break;
//                }
//            }
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                System.out.println("ThreadName: " + Thread.currentThread().getName());
//                userService.saveBatch(userList, batchSize);
//            }, executorService);
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//}
