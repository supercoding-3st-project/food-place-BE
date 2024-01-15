package com.github.foodplacebe.service.authAccount;

import com.github.foodplacebe.config.security.JwtTokenConfig;
import com.github.foodplacebe.config.security.KakaoOauthClient;
import com.github.foodplacebe.repository.userDetails.KakaoMemberResponse;
import com.github.foodplacebe.repository.userRoles.Roles;
import com.github.foodplacebe.repository.userRoles.RolesJpa;
import com.github.foodplacebe.repository.userRoles.UserRoles;
import com.github.foodplacebe.repository.userRoles.UserRolesJpa;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.*;
import com.github.foodplacebe.service.mappers.UserMapper;
import com.github.foodplacebe.web.dto.account.LoginRequest;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.account.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialSignUpService {
    private final UserJpa userJpa;
    private final RolesJpa rolesJpa;
    private final UserRolesJpa userRolesJpa;

    private final KakaoOauthClient kakaoOauthClient;
//    private final AuthenticationManager authenticationManager;

    public void throwingException(String message ,String request){
        throw new NotAcceptableException(message,request);
    }

//    public List<String> login(UserEntity userEntity){
//
//
//        try{
//            if(userEntity.getStatus().equals("delete")){
//                throw new AccessDenied("탈퇴한 계정입니다.",userEntity.getEmail());
//            }
//
//            if(userEntity.getStatus().equals("lock")){
//                LocalDateTime lockDateTime = userEntity.getLockDate();
//                LocalDateTime now = LocalDateTime.now();
//                if(now.isBefore(lockDateTime.plusMinutes(5))){
//                    Duration duration = Duration.between(now, lockDateTime.plusMinutes(5));
//                    String minute = String.valueOf(duration.toMinutes());
//                    String seconds = String.valueOf(duration.minusMinutes(duration.toMinutes()).getSeconds());
//                    throw new AccountLockedException(String.format(
//                            "'%s'님의 계정이 비밀번호 5회 실패로 잠겼습니다. 남은 시간 : %s분 %s초", userEntity.getName(),minute,seconds
//                    ), userEntity.getEmail());
//                }
//            }
//            String email = userEntity.getEmail();
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, userEntity.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            List<String> roles = userEntity.getUserRoles().stream()
//                    .map(u->u.getRoles()).map(r->r.getName()).toList();
//
//            return Arrays.asList(jwtTokenConfig.createToken(email, roles), userEntity.getName());
////        }catch (InternalAuthenticationServiceException e){
////            throw new NotFoundException(String.format("해당 이메일 또는 핸드폰번호 \"%s\"의 계정을 찾을 수 없습니다.", emailOrPhoneNumber));
//        }
//        catch (BadCredentialsException e){
//            throw new CustomBadCredentialsException("비밀번호가 틀립니다. "+e.getMessage(),userEntity.getEmail());
//        }
//    }

    @Transactional(transactionManager = "tm")
    public String signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String phoneNumber = signUpRequest.getPhoneNumber();
        String password = signUpRequest.getPassword();


//        if(!email.matches(".+@.+\\..+")){
//            throw new CustomBindException("CBE","이메일을 정확히 입력해주세요.",email);
////        } else if (!phoneNumber.matches("01\\d{9}")) {
////            throw new CustomBindException("CBE","핸드폰 번호를 확인해주세요.", phoneNumber);
//        } else if (signUpRequest.getNickName().matches("01\\d{9}")){
//            throw new CustomBindException("CBE","핸드폰 번호를 닉네임으로 사용할수 없습니다.",signUpRequest.getNickName());
//        }

//        if(userJpa.existsByEmail(signUpRequest.getEmail())){
//            throw new DuplicateKeyException("DKE","이미 입력하신 "+signUpRequest.getEmail()+" 이메일로 가입된 계정이 있습니다.",signUpRequest.getEmail());
//        }else if(userJpa.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            throw new DuplicateKeyException("DKE","이미 입력하신 "+signUpRequest.getPhoneNumber()+" 핸드폰 번호로 가입된 계정이 있습니다.",signUpRequest.getPhoneNumber());
//        }else if(userJpa.existsByNickName(signUpRequest.getNickName())){
//            throw new DuplicateKeyException("DKE","이미 입력하신 "+signUpRequest.getNickName()+" 닉네임으로 가입된 계정이 있습니다.",signUpRequest.getNickName());
//        }
//        else if(!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
//                ||!(password.length()>=8&&password.length()<=20)
//        ){
//            throw new CustomBindException("CBE","비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",password);
//        }



        signUpRequest.setPassword(password);

        Roles roles = rolesJpa.findByName("ROLE_USER");




        UserEntity userEntity = UserMapper.INSTANCE.signUpRequestToUserEntity(signUpRequest);
        userJpa.save(userEntity);

        userRolesJpa.save(UserRoles.builder()
                .userEntity(userEntity)
                .roles(roles)
                .build());

        SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(userEntity);

        return signUpResponse.getName()+"님 회원 가입이 완료 되었습니다.\n"+
                "가입 날짜 : "+signUpResponse.getJoinDate();
    }

    public String getToken(final String code) {
        final String accessToken = kakaoOauthClient.getAccessToken(code);
        final KakaoMemberResponse response = kakaoOauthClient.getMemberInfo(accessToken);
        return null;

    }

//    public String test(String token) {
//        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest();
//        customUserDetailsService.loadUser()
//    }
}
