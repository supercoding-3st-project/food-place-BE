package com.github.foodplacebe.repository.oauth;

import com.github.foodplacebe.repository.userDetails.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
