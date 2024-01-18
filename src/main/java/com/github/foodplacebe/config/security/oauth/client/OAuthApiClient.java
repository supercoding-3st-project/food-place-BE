package com.github.foodplacebe.config.security.oauth.client;

import com.github.foodplacebe.web.dto.account.oauth.server.OAuthInfoResponse;
import com.github.foodplacebe.web.dto.account.oauth.client.OAuthLoginParams;
import com.github.foodplacebe.repository.userDetails.OAuthProvider;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
