package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.config.security.oauth.client.OAuthApiClient;
import com.github.foodplacebe.web.dto.account.oauth.server.OAuthInfoResponse;
import com.github.foodplacebe.web.dto.account.oauth.client.OAuthLoginParams;
import com.github.foodplacebe.repository.userDetails.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(c->c.oAuthProvider(), Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());

        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}
