package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.repository.users.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialSettingService {

    @Transactional(transactionManager = "tm")
    public void socialIdSet(UserEntity userEntity, Long socialId) {
        userEntity.setSocialId(socialId);
    }
}
