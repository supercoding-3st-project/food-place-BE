package com.github.foodplacebe.config;

import com.github.foodplacebe.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final ScheduleService scheduleService;

    @Scheduled(cron = "1 0 0,12 * * *")
    public void cleanupOldWithdrawnUserAndSetupOldProduct(){
        scheduleService.cleanupOldWithdrawnUser();
        log.info("탈퇴한지 7일 이상된 계정을 삭제 하였습니다.");
        scheduleService.cleanupBlacklistedToken();
        log.info("로그아웃처리된 토큰중 만료된 토큰값들을 삭제 하였습니다.");
    }
}
