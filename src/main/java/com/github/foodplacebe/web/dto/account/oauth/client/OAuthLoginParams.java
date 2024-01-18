package com.github.foodplacebe.web.dto.account.oauth.client;

import com.github.foodplacebe.repository.userDetails.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
