//package com.niuma.langbei.controller;
//
//import com.niuma.langbei.common.BaseResponse;
//import com.niuma.langbei.common.ResultUtils;
//import com.niuma.langbei.model.dto.UserQuery;
//import com.niuma.langbei.model.vo.UserVo;
//import com.niuma.langbei.service.UserSearchService;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author niuma
// * @create 2023-02-22 20:28
// */
//@RestController
//@RequestMapping("/search")
//@CrossOrigin(origins = {"http://175.178.172.77/","https://pc.hb.dogbin.vip/"}, allowCredentials = "true")
//public class SearchController {
//    @Resource
//    UserSearchService userSearchService;
//
//    @PostMapping("searchUser")
//    public BaseResponse<List<UserVo>> searchUsersPage(@RequestBody UserQuery userQuery) {
//        List<UserVo> userVos = userSearchService.findByAll(userQuery.getSearchText());
//        return ResultUtils.success(userVos);
//    }
//}
