package com.github.foodplacebe.config.security.oauth.client;

import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.web.dto.account.oauth.server.KakaoInfoResponse;
import com.github.foodplacebe.config.security.oauth.KakaoTokens;
import com.github.foodplacebe.web.dto.account.oauth.server.OAuthInfoResponse;
import com.github.foodplacebe.web.dto.account.oauth.client.OAuthLoginParams;
import com.github.foodplacebe.repository.userDetails.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.secret}")
    private String secretKey;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", secretKey);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        try {
            KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
            assert response != null;
            return response.getAccessToken();
        }catch (HttpClientErrorException.BadRequest badRequest){
            throw new BadRequestException("전달된 code가 만료되었습니다.", body.getFirst("code"));
        }
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }
}
