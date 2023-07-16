package com.niuma.langbei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niuma.langbei.mapper.UserTeamMapper;
import com.niuma.langbei.model.domain.UserTeam;
import com.niuma.langbei.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * @author niumazlb
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2022-08-22 10:32:15
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




