package com.booboil.partner.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.booboil.partner.model.domain.Team;
import com.booboil.partner.model.domain.User;
import com.booboil.partner.model.dto.TeamQuery;
import com.booboil.partner.model.dto.request.TeamJoinRequest;
import com.booboil.partner.model.dto.request.TeamQuitRequest;
import com.booboil.partner.model.dto.request.TeamUpdateRequest;
import com.booboil.partner.model.vo.TeamUserVo;

import java.util.List;

/**
 * @author niumazlb
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2022-08-22 10:32:26
 */
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    Boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除解散队伍
     *
     * @param id
     * @return
     */
    Boolean deleteTeam(Long id, User loginUser);

    /**
     * 通过id队伍
     *
     * @param id
     * @return
     */
    TeamUserVo getTeamById(long id, boolean isAdmin, User loginUser);


    List<Team> getTeamByUserId(Long userId);
}
