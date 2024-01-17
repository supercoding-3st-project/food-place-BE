package com.github.foodplacebe.web.dto.account.oauth.server;

import com.github.foodplacebe.repository.userDetails.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickName();
    String getProfileImg();
    OAuthProvider getOAuthProvider();
}
