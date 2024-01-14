package com.github.accountmanagementproject.service.authAccount;

import com.github.accountmanagementproject.repository.userRoles.Roles;
import com.github.accountmanagementproject.repository.userRoles.RolesJpa;
import com.github.accountmanagementproject.repository.userRoles.UserRoles;
import com.github.accountmanagementproject.repository.userRoles.UserRolesJpa;
import com.github.accountmanagementproject.repository.users.UserEntity;
import com.github.accountmanagementproject.repository.users.UserJpa;
import com.github.accountmanagementproject.service.mappers.UserMapper;
import com.github.accountmanagementproject.web.dto.account.SignUpRequest;
import com.github.accountmanagementproject.web.dto.account.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialSignUpService {
    private final UserJpa userJpa;
    private final RolesJpa rolesJpa;
    private final UserRolesJpa userRolesJpa;
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

//    public String test(String token) {
//        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest();
//        customUserDetailsService.loadUser()
//    }
}
