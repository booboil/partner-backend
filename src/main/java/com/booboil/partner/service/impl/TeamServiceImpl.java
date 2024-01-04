package com.booboil.partner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.booboil.partner.common.ErrorCode;
import com.booboil.partner.exception.BusinessException;
import com.booboil.partner.mapper.TeamMapper;
import com.booboil.partner.model.domain.Team;
import com.booboil.partner.model.domain.User;
import com.booboil.partner.model.domain.UserTeam;
import com.booboil.partner.model.dto.TeamQuery;
import com.booboil.partner.model.enums.TeamStatusEnum;
import com.booboil.partner.model.dto.request.TeamJoinRequest;
import com.booboil.partner.model.dto.request.TeamQuitRequest;
import com.booboil.partner.model.dto.request.TeamUpdateRequest;
import com.booboil.partner.model.vo.TeamUserVo;
import com.booboil.partner.model.vo.UserVo;
import com.booboil.partner.service.TeamService;
import com.booboil.partner.service.UserService;
import com.booboil.partner.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author niumazlb
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2022-08-22 10:32:26
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // 1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // 3. 校验信息
        //   1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //   2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //   3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }

        //   4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //   5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        // 6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null) {
            if (new Date().after(expireTime)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
            }
        }
        // 7. 校验用户最多创建 5 个队伍
        // todo 有 bug，可能同时创建 100 个队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = userTeamService.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建和加入 5 个队伍");
        }
        // 8. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        // 9. 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        //组合查询条件
        if (teamQuery != null) {
            //根据id查询
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }

            List<Long> ids = teamQuery.getIds();
            if (!CollectionUtils.isEmpty(ids)) {
                queryWrapper.in("id", ids);
            }

            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            //根据队伍名查询
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            //根据队伍描述查询
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            //根据队长ID查询
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            //根据状态查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            List<Integer> statusArr = new ArrayList<>(3);
            if (statusEnum == null) {
                statusArr.add(TeamStatusEnum.PUBLIC.getValue());
                statusArr.add(TeamStatusEnum.SECRET.getValue());
            }else {
                statusArr.add(statusEnum.getValue());
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.in("status", statusArr);

            //根据最大人数查询
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
        }
        //不展示已过期队伍
        queryWrapper.and(qw -> qw.isNull("expireTime").or().gt("expireTime", new Date()));

        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVo> teamUserVos = new ArrayList<>();


        //关联查询队伍成员信息
        for (Team team : teamList) {
            Long teamId = team.getId();
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("teamId", teamId);
            //拿到每个队伍加入的成员id
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            // 根据userid 查询user 拿到加入该队伍的成员列表
            List<UserVo> userList = userTeamList.stream().map(userTeam -> {
                Long userId = userTeam.getUserId();
                User user = userService.getById(userId);
                User safetyUser = userService.getSafetyUser(user);
                UserVo UserVo = new UserVo();
                if (safetyUser != null) {
                    BeanUtils.copyProperties(safetyUser, UserVo);
                }
                return UserVo;
            }).filter(userVo -> StringUtils.isNotEmpty(userVo.getUserAccount())).collect(Collectors.toList());

            // 创建返回对象
            TeamUserVo teamUserVo = new TeamUserVo();
            // 将队伍公告脱敏
            team.setAnnounce("");

            BeanUtils.copyProperties(team, teamUserVo);
            // 设置已加入的成员
            teamUserVo.setJoinUserList(userList);
            userList.forEach(userVo -> {
                if (userVo.getId() == team.getUserId()) {
                    teamUserVo.setCreateUser(userVo);
                }
            });
            // 加入返回列表
            teamUserVos.add(teamUserVo);
        }
        return teamUserVos;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        Long id = teamUpdateRequest.getId();
        if (teamUpdateRequest == null || id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = getTeamById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        TeamStatusEnum oldStatusEnum = TeamStatusEnum.getEnumByValue(oldTeam.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET) && oldStatusEnum.equals(TeamStatusEnum.PUBLIC)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要有密码！");
            }
        }
        if(teamUpdateRequest.getMaxNum() != null){
            int maxNum = Optional.ofNullable(teamUpdateRequest.getMaxNum()).orElse(0);
            if (maxNum < 1 || maxNum > 20 || maxNum < oldTeam.getMaxNum()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
            }
        }
        if(teamUpdateRequest.getAnnounce() != null){
            if (teamUpdateRequest.getAnnounce().length()>80) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告长度不能超过80字");

            }
        }

        Team team = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, team);
        boolean res = this.updateById(team);
        return res;
    }

    @Override
    public Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();

        Team team = getTeamById(teamId);
        //校验过期时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        //禁止加入私有队伍
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(team.getStatus());
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "禁止加入私有队伍");
        }
        //加密队伍校验密码
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误！");
            }
        }
        //查询当前用户加入队伍总数,最多创建/加入5个队伍
        long userId = loginUser.getId();
        RLock lock = redissonClient.getLock("langbei:join_team");
        try {
            while (true) {
                if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                    QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("userId", userId);
                    long hasJoinNum = userTeamService.count(queryWrapper);
                    if (hasJoinNum >= 5) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建和加入5个队伍！");
                    }
                    //不能重复加入
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("userId", userId);
                    queryWrapper.eq("teamId", teamId);
                    long hasUserJoinTeam = userTeamService.count(queryWrapper);
                    if (hasUserJoinTeam > 0) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已加入！");
                    }
                    //已加入队伍人数
                    long teamHasJoinNum = countTeamUserByTeamId(teamId);
                    if (teamHasJoinNum > team.getMaxNum()) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满！");
                    }

                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {

        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = getTeamById(teamId);
        long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
        }
        long teamHasJoinNum = countTeamUserByTeamId(teamId);
        //队伍只剩一人
        if (teamHasJoinNum == 1) {
            //删除队伍信息和关系
            this.removeById(teamId);
        } else {
            //如果是队长
            if (team.getUserId() == userId) {
                //找出加入队伍的人，只用取两条，一个是我一个是顺位的人
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.eq("teamId", teamId);
                userTeamQueryWrapper.last("order by id asc limit2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                Team updateTeam = new Team();
                updateTeam.setUserId(nextTeamLeaderId);
                updateTeam.setId(teamId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队长失败");
                }
            }
        }
        //移除关系
        return userTeamService.remove(queryWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTeam(Long id, User loginUser) {
        //1. 校验队伍
        Team team = getTeamById(id);
        //2.校验身份
        if (team.getUserId() != loginUser.getId()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "你不是队长，你在搞什么飞机");
        }
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        long teamId = team.getId();
        userTeamQueryWrapper.eq("teamId", teamId);
        //移除关系
        boolean res = userTeamService.remove(userTeamQueryWrapper);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联失败");
        }
        //删除队伍
        return this.removeById(teamId);
    }

    @Override
    public TeamUserVo getTeamById(long id, boolean isAdmin, User loginUser) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(id);

        Long teamId = team.getId();
        Long userId = team.getUserId();

        TeamUserVo teamUserVo = new TeamUserVo();
        BeanUtils.copyProperties(team, teamUserVo);
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
        //设置已加入人数
        teamUserVo.setHasJoinNum(userTeamList.size());
        List<UserVo> memberUser = userTeamList.stream().map(userTeam -> {
            Long memberId = userTeam.getUserId();
            User user = userService.getById(memberId);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            if (memberId.equals(userId)) {
                //设置创建人信息
                teamUserVo.setCreateUser(userVo);
            }
            if (loginUser.getId() == memberId) {
                teamUserVo.setHasJoin(true);
            }
            return userVo;
        }).collect(Collectors.toList());

        if (!(TeamStatusEnum.PUBLIC.getValue() == team.getStatus()) && !teamUserVo.isHasJoin()) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        //设置队伍成员
        teamUserVo.setJoinUserList(memberUser);
        return teamUserVo;
    }

    @Override
    public List<Team> getTeamByUserId(Long userId) {
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, UserTeam::getUserId, userId);
        List<UserTeam> userTeams = userTeamService.list(queryWrapper);
        if(userTeams.size()<=0){
            return null;
        }
        List<Long> teamIdList = userTeams.stream().map(UserTeam::getTeamId).collect(Collectors.toList());
        List<Team> teamList = this.listByIds(teamIdList);
        return teamList;
    }


    private long countTeamUserByTeamId(long teamId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        long teamHasJoinNum = userTeamService.count(queryWrapper);
        return teamHasJoinNum;
    }

    /**
     * 根据ID查队伍
     *
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查询队伍
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在！");
        }
        return team;
    }

}






