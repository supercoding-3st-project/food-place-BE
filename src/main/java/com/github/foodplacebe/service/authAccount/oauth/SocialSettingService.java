package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialSettingService {
    private final UserJpa userJpa;

    @Transactional(transactionManager = "tm")
    public void socialIdSet(UserEntity userEntity, Long socialId) {
        userEntity.setSocialId(socialId);
    }
}
