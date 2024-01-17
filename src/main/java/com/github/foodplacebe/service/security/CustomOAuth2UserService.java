//package com.github.foodplacebe.service.security;//package com.github.accountmanagementproject.service.security;
//
//import com.github.foodplacebe.config.security.JwtTokenConfig;
//import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
//import com.github.foodplacebe.repository.users.UserEntity;
//import com.github.foodplacebe.repository.users.UserJpa;
//import com.github.foodplacebe.service.authAccount.SocialSignUpService;
//import com.github.foodplacebe.service.exceptions.NotAcceptableException;
//import com.github.foodplacebe.web.dto.account.SignUpRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.stream.Collectors;//
////import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
////import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
////import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
////import org.springframework.security.oauth2.core.user.OAuth2User;
////
//@RequiredArgsConstructor
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    private final UserJpa userJpa;
//    private final SocialSignUpService socialSignUpService;
//    private final JwtTokenConfig jwtTokenConfig;
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User user = super.loadUser(userRequest);
//    String provider = userRequest.getClientRegistration().getRegistrationId();
//    Map<String, Object> userInfo = user.getAttributes();
//
//    Long userId = null;
//    String email = null;
//    String name = null;
//    String img = null;
//
//    // **
//    switch (provider) {
//        case "kakao":
//            Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
//            userId = (Long) userInfo.get("id");
//            email = (String) kakaoAccount.get("email");
//            name = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
//            img = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("profile_image_url");
//            break;
//    }
//
//
//    String finalEmail = email;
////        Pattern pattern = Pattern.compile("\\d+");
////        Matcher matcher = pattern.matcher(userId);
////        StringBuilder result = new StringBuilder();
////        while (matcher.find()){
////            result.append(matcher.group());
////        }
//    UserEntity userEntity = userJpa.findBySocialIdJoin(userId);
//    try{
//        if(userEntity == null){
//            userEntity = userJpa.findByEmail(email);
//            if(userEntity!=null){
//                boolean userWantsConnect = false;
//                if(userWantsConnect){
//                    userEntity.setSocialId(userId);
//                }else{
//                    socialSignUpService.throwingException(email+" 계정에 소셜 아이디 연결을 취소 하였습니다.",userId.toString());
////                    throw new NotAcceptableException(email+" 계정에 소셜 아이디 연결을 취소 하였습니다.",userId.toString());
//                }
//            }
//        }
//        if (userEntity == null){
//            boolean userWantsSignUp = true;
//            if(userWantsSignUp){
//
//            }else{
//                socialSignUpService.throwingException(email+" 계정에 소셜 아이디 연결을 취소 하였습니다.",userId.toString());
//                throw new NotAcceptableException("소셜 회원가입을 취소하였습니다.",userId.toString());
//            }
//        }
//    }catch (Exception ex){
//        ex.printStackTrace();
//        socialSignUpService.throwingException(email+" 계정에 소셜 아이디 연결을 취소 하였습니다.",userId.toString());
//        throw new NotAcceptableException("잡", userId.toString());
//    }
//
//
//    SignUpRequest signUpRequest = new SignUpRequest();
//    String token = jwtTokenConfig.createToken(email,
//            userEntity
//                    .getUserRoles()
//                    .stream()
//                    .map(userRoles -> userRoles.getRoles())
//                    .map(roles -> roles.getName()).toList()
//    );
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("Token", token);
//
//
//
//
//
//
//    return CustomUserDetails.builder()
//            .userId(userEntity.getUserId())
//            .email(userEntity.getEmail())
//            .password(userEntity.getPassword())
//            .phoneNumber(userEntity.getPhoneNumber())
//            .nickName(userEntity.getNickName())
//            .name(userEntity.getName())
//
//            .authorities(userEntity.getUserRoles()
//                    .stream().map(ur->ur.getRoles())
//                    .map(r->r.getName())
//                    .collect(Collectors.toList()))
//            .build();
//    }
//}
