package com.github.accountmanagementproject.service.authAccount;

import com.github.accountmanagementproject.repository.users.UserEntity;
import com.github.accountmanagementproject.repository.users.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class AccountLockService {
    private final UserJpa userJpa;

    @Transactional(transactionManager = "tm")
    public Map<String, String> failureCount(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        UserEntity userEntity = userJpa.findByEmail(email);
        Map<String, String> requestInfo = new HashMap<>();
        requestInfo.put("request", null);
        if(userEntity.getLockDate() != null &&
                LocalDateTime.now().isAfter(userEntity.getLockDate().plusMinutes(5))
        ) {
            requestInfo.replace("request",null,"unlock");
            userEntity.setFailureCount(userEntity.getFailureCount() + 1);
            return requestInfo;
        }
        if(userEntity.getFailureCount()<4) {
            userEntity.setFailureCount(userEntity.getFailureCount() + 1);
            userEntity.setLockDate(LocalDateTime.now());
            requestInfo.replace("request",null,"increment");
            requestInfo.put("remaining", String.valueOf(5-userEntity.getFailureCount()));
            return requestInfo;
        } else {
            userEntity.setStatus("lock");
            userEntity.setLockDate(LocalDateTime.now());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lockDateTime = userEntity.getLockDate();

            Duration duration = Duration.between(now, lockDateTime.plusMinutes(5));
            String minute = String.valueOf(duration.toMinutes());
            String seconds = String.valueOf(duration.minusMinutes(duration.toMinutes()).getSeconds());
            requestInfo.replace("request",null, "locked");
            requestInfo.put("name", userEntity.getName());
            requestInfo.put("minute", minute);
            requestInfo.put("seconds", seconds);
            return requestInfo;
        }
    }

    @Transactional(transactionManager = "tm")
    public void resetFailureCount(String email){
        UserEntity userEntity = userJpa.findByEmail(email);
        userEntity.setFailureCount(0);
        userEntity.setLockDate(null);
        userEntity.setStatus("normal");
    }
}
