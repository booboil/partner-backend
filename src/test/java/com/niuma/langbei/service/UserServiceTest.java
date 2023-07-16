//package com.niuma.langbei.service;
//import com.niuma.langbei.model.domain.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author niumazlb
// * @create 2022-03-12 21:23
// */
//@SpringBootTest
//class UserServiceTest {
//  @Resource private UserService userService;
//
//  @Test
//  public void testAddUser() {
//    User user = new User();
//    user.setUsername("niuma");
//    user.setUserAccount("123");
//    user.setAvatarUrl("https://mvnrepository.com/img/73fb51f61e7d12b766d8dfaf3bf16b90");
//    user.setGender(0);
//    user.setUserPassword("xxx");
//    user.setPhone("123");
//    user.setEmail("456");
//
//    boolean result = userService.save(user);
//    System.out.println(user.getId());
//    Assertions.assertTrue(result);
//  }
//
////  @Test
////  void userRegister() {
////    String userAccount = "niuma";
////    String userPassword = "";
////    String checkPassword = "123456";
////    String planetCode = "520";
////    long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    userAccount = "niu";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    userAccount = "niuma";
////    userPassword= "123456";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    userAccount = "niu ma";
////    userPassword = "12345678";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    checkPassword = "123456789";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    userAccount = "123";
////    checkPassword="12345678";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertEquals(-1,result);
////    userAccount = "niuma";
////    result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
////    Assertions.assertTrue(result>0);
////
////  }
//
//  @Test
//  void testSearchUsersByTags(){
//    List<String> tagName = Arrays.asList("Java");
//    List<User> users = userService.searchUsersByTags(tagName);
//    System.out.println(users);
//  }
//}
