package com.booboil.partner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.booboil.partner.mapper.UserTeamMapper;
import com.booboil.partner.model.domain.UserTeam;
import com.booboil.partner.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * @author booboil
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




