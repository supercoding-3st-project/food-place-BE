package com.github.foodplacebe.repository.oauth;

import com.github.foodplacebe.repository.userDetails.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickName();
    OAuthProvider getOAuthProvider();
}
