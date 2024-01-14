package com.github.foodplacebe.service.security;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.authAccount.SocialSignUpService;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService extends DefaultOAuth2UserService implements UserDetailsService {
    private final UserJpa userJpa;
    private final SocialSignUpService socialSignUpService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userJpa.findByEmailJoin(email).orElseThrow(() ->
                new NotFoundException("(토큰에러) 해당 이메일을 찾을 수 없습니다.", email));

        return CustomUserDetails.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .nickName(userEntity.getNickName())
                .name(userEntity.getName())

                .authorities(userEntity.getUserRoles()
                        .stream().map(ur->ur.getRoles())
                        .map(r->r.getName())
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> userInfo = user.getAttributes();

        String userId = null;
        String email = null;
        String name = null;
        String img = null;

        // **
        switch (provider) {
            case "kakao":
                Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
                userId = provider + "_" + userInfo.get("id");
                email = (String) kakaoAccount.get("email");
                name = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
                img = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("profile_image_url");
                break;
        }

        log.info("provider: {} -> userId : {}, name : {}, email : {}", provider, userId, name, email);

        String finalEmail = email;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(userId);
        StringBuilder result = new StringBuilder();
        while (matcher.find()){
            result.append(matcher.group());
        }

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setImageUrl(img);
        signUpRequest.setEmail(email);
        signUpRequest.setPhoneNumber(result.toString());
        signUpRequest.setPassword("패스워드설정안됨");
        signUpRequest.setName(name);
        signUpRequest.setNickName(userId);
        UserEntity userEntity = userJpa.findByEmailJoin(email)
                .orElseGet(()->{
//                    String msg = socialSignUpService.signUp(signUpRequest);
//                    log.info(msg);
//                    return userJpa.findByEmailJoin(finalEmail).orElseThrow(()->new NotFoundException("NFE", "Not Found Email", finalEmail));
                    return null;
                });

        return CustomUserDetails.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .nickName(userEntity.getNickName())
                .name(userEntity.getName())

                .authorities(userEntity.getUserRoles()
                        .stream().map(ur->ur.getRoles())
                        .map(r->r.getName())
                        .collect(Collectors.toList()))
                .build();
    }


}
