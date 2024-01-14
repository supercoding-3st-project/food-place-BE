package com.github.foodplacebe.service.security;//package com.github.accountmanagementproject.service.security;
//
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        String accessToken = userRequest.getAccessToken().getTokenValue();
//        String userInfoEndpointUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
//
//        return null;
//    }
//}
