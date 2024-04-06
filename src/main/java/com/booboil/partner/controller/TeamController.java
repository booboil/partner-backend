package com.booboil.partner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booboil.partner.common.BaseResponse;
import com.booboil.partner.common.DeleteRequest;
import com.booboil.partner.common.ErrorCode;
import com.booboil.partner.common.ResultUtils;
import com.booboil.partner.exception.BusinessException;
import com.booboil.partner.model.domain.Team;
import com.booboil.partner.model.domain.User;
import com.booboil.partner.model.domain.UserTeam;
import com.booboil.partner.model.dto.TeamQuery;
import com.booboil.partner.model.dto.request.TeamAddRequest;
import com.booboil.partner.model.dto.request.TeamJoinRequest;
import com.booboil.partner.model.dto.request.TeamQuitRequest;
import com.booboil.partner.model.dto.request.TeamUpdateRequest;
import com.booboil.partner.model.vo.TeamUserVo;
import com.booboil.partner.service.TeamService;
import com.booboil.partner.service.UserService;
import com.booboil.partner.service.UserTeamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author booboil
 */
@RestController
@RequestMapping("/team")
//跨域
//@CrossOrigin(origins = {"http://175.178.172.77/"}, allowCredentials = "true")
public class TeamController {

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    @ApiOperation("创建队伍")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);

    }

    @PostMapping("/update")
    @ApiOperation("更新队伍")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获取队伍信息")
    public BaseResponse<TeamUserVo> getTeamById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);

        TeamUserVo teamUserVo = teamService.getTeamById(id, true, loginUser);

        if (teamUserVo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamUserVo);
    }

    @GetMapping("/list")
    @ApiOperation("查询已加入的队伍信息")
    public BaseResponse<List<TeamUserVo>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.getLoginUser(request);
        // 只有管理员查看加密队伍
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery, isAdmin);
        if (teamList.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "未找到队伍信息");
        }
        queryTeamCount(request, teamList);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    @ApiOperation("分页查询队伍信息")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(teamPage);
    }

    @PostMapping("/join")
    @ApiOperation("加入队伍")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean res = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(res);
    }

    @PostMapping("/quit")
    @ApiOperation("退出队伍")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean res = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(res);
    }

    @PostMapping("/delete")
    @ApiOperation("删除队伍")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {

        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.deleteTeam(id, loginUser);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/list/user")
    @ApiOperation("")
    public BaseResponse<List<Team>> getTeamListByUserId(Long userId){
        if(userId == null || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Team> teamList = teamService.getTeamByUserId(userId);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我创建的队伍
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVo>> listMyCreateTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());

        List<TeamUserVo> teamList = teamService.listTeams(teamQuery, true);
        queryTeamCount(request, teamList);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我加入的队伍
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVo>> listMyJoinTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);

        //按teamId分组，返回Map，Map的key就是去重后的teamId
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));

        ArrayList<Long> ids = new ArrayList<>(listMap.keySet());

        teamQuery.setIds(ids);

        List<TeamUserVo> teamList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(ids)){
            teamList = teamService.listTeams(teamQuery, true);
            queryTeamCount(request, teamList);
        }
        return ResultUtils.success(teamList);
    }

    /**
     * 填充队伍人数字段
     *
     * @param request
     * @param teamList
     */
    private void queryTeamCount(HttpServletRequest request, List<TeamUserVo> teamList) {
        //条件查询出的队伍列表
        List<Long> teamIdList = teamList.stream().map(TeamUserVo::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        try {
            User loginUser = userService.getLoginUser(request);
            userTeamQueryWrapper.eq("userId", loginUser.getId());
            userTeamQueryWrapper.in("teamId", teamIdList);
            //已加入队伍集合
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            //已加入的队伍的id集合
            Set<Long> hasJoinTeamIdList = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team -> {
                boolean hasJoin = hasJoinTeamIdList.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }

        List<UserTeam> userTeamJoinList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(teamIdList)){
            QueryWrapper<UserTeam> userTeamJoinQueryWrapper = new QueryWrapper<>();
            userTeamJoinQueryWrapper.in("teamId", teamIdList);
            userTeamJoinList = userTeamService.list(userTeamJoinQueryWrapper);
        }

        //按每个队伍Id分组
        Map<Long, List<UserTeam>> teamIdUserTeamList = userTeamJoinList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));

        teamList.forEach(team -> {
            team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size());
        });
    }
}
