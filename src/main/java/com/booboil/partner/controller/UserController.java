package com.booboil.partner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booboil.partner.common.BaseResponse;
import com.booboil.partner.common.DeleteRequest;
import com.booboil.partner.common.ErrorCode;
import com.booboil.partner.common.ResultUtils;
import com.booboil.partner.exception.BusinessException;
import com.booboil.partner.model.domain.User;
import com.booboil.partner.model.domain.UserTeam;
import com.booboil.partner.model.dto.UserQuery;
import com.booboil.partner.model.dto.request.UserLoginRequest;
import com.booboil.partner.model.dto.request.UserRegisterRequest;
import com.booboil.partner.model.vo.UserVo;
import com.booboil.partner.service.UserService;
import com.booboil.partner.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.booboil.partner.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author booboil
 */
@RestController
@RequestMapping("/user")
//跨域
//@CrossOrigin(origins = {"http://175.178.172.77/"}, allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserTeamService userTeamService;

//    @Resource
//    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//      return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        String avatarUrl = userRegisterRequest.getAvatarUrl();
        if (!StringUtils.isNumeric(planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号必须为数字噢！");
        }
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = userService.userRegister(userAccount, userPassword, checkPassword, planetCode, avatarUrl);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(
            @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);

        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int userLogout = userService.userLogout(request);
        return ResultUtils.success(userLogout);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User userCurrent = (User) userObj;
        if (userCurrent == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User user = userService.getById(userCurrent.getId());
        // TODO 校验用户是否合法
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream()
                .map(user -> userService.getSafetyUser(user))
                .collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/searchPage")
    public BaseResponse<Page<User>> searchUsersPage(@RequestBody UserQuery userQuery) {
        String searchText = userQuery.getSearchText();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("username", searchText)
                    .or().like("profile", searchText)
                    .or().like("tags", searchText);
        }
        Page<User> page = new Page<>(userQuery.getPageNum(), userQuery.getPageSize());
        Page<User> userListPage = userService.page(page, queryWrapper);
        List<User> userList = userListPage.getRecords();
        List<User> safetyUserList = userList.stream()
                .map(user -> userService.getSafetyUser(user))
                .collect(Collectors.toList());
        userListPage.setRecords(safetyUserList);
        return ResultUtils.success(userListPage);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagList, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.getLoginUser(request);
        List<User> userList = userService.searchUsersByTags(tagList);
        return ResultUtils.success(userList);
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<User> userPage ;
//        String redisKey = String.format("partner:user:recommend:%s", loginUser.getId());
//        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(redisKey);
//        if (userPage != null) {
//            return ResultUtils.success(userPage);
//        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        redisTemplate.opsForValue().set(redisKey, userPage, 30, TimeUnit.MINUTES);
        return ResultUtils.success(userPage);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);

        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    @Transactional
    public BaseResponse<Boolean> deleteUsers(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        long id = deleteRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id小于0");
        }
        boolean result = userService.removeById(id);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        boolean removeUserTeam = userTeamService.remove(queryWrapper);
        if(result && removeUserTeam){
//            rabbitTemplate.convertAndSend(MqConstants.USER_EXCHANGE,MqConstants.USER_DELETE_KEY,id);
        }
        return ResultUtils.success(result && removeUserTeam);
    }

    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<User> matchUsers = userService.matchUsers(num, loginUser);
        return ResultUtils.success(matchUsers);
    }

    @PostMapping("/getUserListByIds")
    public BaseResponse<List<UserVo>> getUserListByIds(@RequestBody UserQuery userQuery){
        List<User> userList = userService.listByIds(userQuery.getIds());
        List<UserVo> userVoList = userList.stream().map(user -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVoList);
    }

}
