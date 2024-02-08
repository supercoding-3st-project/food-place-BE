package com.github.foodplacebe.service;

import com.github.foodplacebe.repository.users.BlacklistedToken;
import com.github.foodplacebe.repository.users.BlacklistedTokenJpa;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserJpa userJpa;
    private final BlacklistedTokenJpa blacklistedTokenJpa;


    //탈퇴한지 7일 이상된 계정 정보 자동 삭제 (하루에 두번씩 로직 실행됨)
    @Transactional(transactionManager = "tm")
    public void cleanupOldWithdrawnUser() {
        List<UserEntity> userEntities = userJpa.findAll();
        List<UserEntity> oldWithdrawnUser = userEntities.stream().filter(ue->ue.getDeletionDate()!=null)
                .filter(ue-> ChronoUnit.DAYS.between(ue.getDeletionDate(), LocalDateTime.now())>=7).toList();
        if (oldWithdrawnUser.isEmpty()) return;

        userJpa.deleteAll(oldWithdrawnUser);
    }

    @Transactional(transactionManager = "tm")
    public void cleanupBlacklistedToken() {
        List<BlacklistedToken> blacklistedTokens = blacklistedTokenJpa.findAll();
        List<BlacklistedToken> expirationTokens = blacklistedTokens.stream()
                .filter(et->et.getExpirationTime().isBefore(LocalDateTime.now()))
                .toList();
        if(expirationTokens.isEmpty()) return;
        blacklistedTokenJpa.deleteAll(expirationTokens);
    }
}
