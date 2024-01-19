package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.config.security.JwtTokenConfig;
import com.github.foodplacebe.web.dto.account.SocialAccountDto;
import com.github.foodplacebe.web.dto.account.oauth.server.OAuthInfoResponse;
import com.github.foodplacebe.web.dto.account.oauth.client.OAuthLoginParams;
import com.github.foodplacebe.repository.userRoles.RolesJpa;
import com.github.foodplacebe.repository.userRoles.UserRolesJpa;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.*;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialSignUpService {
    private final UserJpa userJpa;
    private final RolesJpa rolesJpa;
    private final UserRolesJpa userRolesJpa;
    private final JwtTokenConfig jwtTokenConfig;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final SocialSettingService socialSettingService;




    public List<String> login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        String email =oAuthInfoResponse.getEmail();
        String profileImg = oAuthInfoResponse.getProfileImg();
        String socialProvider = oAuthInfoResponse.getOAuthProvider().name();
        String nickname = oAuthInfoResponse.getNickName();
        Long socialId = oAuthInfoResponse.getSocialId();
        UserEntity userEntity = null;

        switch (socialProvider){
            case "KAKAO":
                userEntity = userJpa.findBySocialIdJoin(socialId)
                        .orElseThrow(()->{
                            UserEntity userEntityEmail =  userJpa.findByEmailJoin(email)
                                    .orElseThrow(()->{
                                        SocialAccountDto socialAccountDto = new SocialAccountDto();
                                        socialAccountDto.setProvider(socialProvider);
                                        socialAccountDto.setSocialId(socialId);
                                        socialAccountDto.setEmail("("+socialProvider+") "+email);
                                        socialAccountDto.setName(nickname);
                                        socialAccountDto.setImageUrl(profileImg);

                                        socialSettingService.makeSocialTemp(socialAccountDto);
                                        return new NotFoundSocialAccount(socialAccountDto, "회원가입이 필요합니다.");
                                    });
                            socialSettingService.socialIdSet(userEntityEmail, socialId);
                            return new ConflictException("소셜 이메일과 동일한 이메일로 가입된 계정이 있습니다. 계정 연결이 필요합니다.", email);
                        });
                if(userEntity.getStatus().equals("signing")){
                    SocialAccountDto socialAccountDto = new SocialAccountDto();
                    socialAccountDto.setProvider(socialProvider);
                    socialAccountDto.setSocialId(userEntity.getSocialId());
                    socialAccountDto.setEmail(userEntity.getEmail());
                    socialAccountDto.setName(userEntity.getName());
                    socialAccountDto.setImageUrl(userEntity.getImageUrl());
                    throw new NotFoundSocialAccount(socialAccountDto,"회원가입이 완료되지 않았습니다.");
                }
                break;
            case "NAVER":
                break;
            case "GOOGLE":
                break;
        }
        List<String> roles = userEntity.getUserRoles().stream()
                .map(u->u.getRoles()).map(r->r.getName()).toList();



        return Arrays.asList(jwtTokenConfig.createToken(userEntity.getEmail(), roles), userEntity.getName());
    }

    public ResponseEntity<ResponseDto> connectAccount(boolean isConnect, Long socialId) {

        if(isConnect) {
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.CREATED.value(),
                    "소셜 연결이 완료 되었습니다."),
                    HttpStatus.CREATED
            );
        }else {
            socialSettingService.cancelConnect(socialId);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.ACCEPTED.value(),
                            "소셜 연결이 취소 되었습니다."),
                    HttpStatus.ACCEPTED
            );
        }
    }


    public ResponseEntity<ResponseDto> socialSignUpFix(boolean isSignUp, Long socialId, SignUpRequest signUpRequest) {
        UserEntity userEntity = userJpa.findBySocialIdJoin(socialId)
                .orElseThrow(()-> new NotFoundException("가입중인 계정이 없습니다.", socialId.toString()));
        if(!isSignUp){
            socialSettingService.deleteSigningUpAccount(userEntity);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.ACCEPTED.value(),
                            "소셜 가입을 취소 하였습니다."),
                    HttpStatus.ACCEPTED
            );
        }else{
            socialSettingService.loadSigningUpAccount(userEntity, signUpRequest);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.CREATED.value(),
                            userEntity.getNickName()+"님 소셜 가입이 완료 되었습니다."),
                    HttpStatus.CREATED
            );
        }
    }
}

