package com.niuma.langbei.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.langbei.model.domain.Team;
import com.niuma.langbei.model.domain.UserTeam;
import com.niuma.langbei.service.TeamService;
import com.niuma.langbei.service.UserTeamService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author niumazlb
 * @create 2022-12-09 16:49
 */
@Component
public class UpdateTeamJob {

    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;


    @Resource
    private RedissonClient redissonClient;


    @Scheduled(cron = "0 */10 * * * ?")
    public void doUpdateTeam() {
        RLock lock = redissonClient.getLock("langbei:preUpdateTeamJob:doUpdate:lock");
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
                queryWrapper.and(qw -> qw.isNotNull("expireTime").and(teamQueryWrapper -> teamQueryWrapper.lt("expireTime", new Date())));
                List<Team> expireTimeTeams = teamService.list(queryWrapper);
                if (expireTimeTeams.isEmpty()) {
                    return;
                }
                List<Long> expireTimeTeamsId = expireTimeTeams.stream().map(team -> {
                    Long teamId = team.getId();
                    return teamId;
                }).collect(Collectors.toList());
                if (expireTimeTeamsId.isEmpty()) {
                    return;
                }
                queryWrapper.in("id", expireTimeTeamsId);
                teamService.remove(queryWrapper);
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.in("teamId", expireTimeTeamsId);
                userTeamService.remove(userTeamQueryWrapper);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
