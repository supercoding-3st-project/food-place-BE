package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.config.security.JwtTokenConfig;
import com.github.foodplacebe.service.mappers.UserMapper;
import com.github.foodplacebe.web.dto.account.SignUpResponse;
import com.github.foodplacebe.web.dto.account.SocialAccountDto;
import com.github.foodplacebe.web.dto.account.oauth.server.OAuthInfoResponse;
import com.github.foodplacebe.web.dto.account.oauth.client.OAuthLoginParams;
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
    private final JwtTokenConfig jwtTokenConfig;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final SocialSettingService socialSettingService;




    public List<Object> login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        String email =oAuthInfoResponse.getEmail();
        String profileImg = oAuthInfoResponse.getProfileImg();
        String socialProvider = oAuthInfoResponse.getOAuthProvider().name();
        String nickname = oAuthInfoResponse.getNickName();
        Long socialId = oAuthInfoResponse.getSocialId();

        UserEntity userEntity = userJpa.findBySocialId(socialId);
        if (userEntity==null){
            userEntity = userJpa.findBySocialId(socialId+3000000000000000000L);
            if(userEntity!=null){
                throw new ConflictException("소셜 계정과 연동 시키다 취소된 계정이 있습니다. 연동 시키겠습니까?", String.valueOf(userEntity.getSocialId()));
            }
        }
        if(userEntity!=null){
            if(userEntity.getStatus().equals("temp")){
                SocialAccountDto socialAccountDto = new SocialAccountDto();
                socialAccountDto.setProvider(socialProvider);
                socialAccountDto.setSocialId(userEntity.getSocialId());
                socialAccountDto.setEmail(email);
                socialAccountDto.setImageUrl(userEntity.getImageUrl());
                socialAccountDto.setNickName(userEntity.getNickName());
                throw new NotFoundSocialAccount(socialAccountDto,"회원가입이 완료되지 않았습니다. 이어서 하시겠습니까?");
            }
//            else if(userEntity.getStatus().equals("connect"))
        }
        switch (socialProvider){
            case "KAKAO":
                userEntity = userJpa.findBySocialIdJoin(socialId)
                        .orElseThrow(()->{
                            UserEntity userEntityEmail =  userJpa.findByEmailJoin(email)
                                    .orElseThrow(()->{
                                        SocialAccountDto socialAccountDto = new SocialAccountDto();
                                        socialAccountDto.setProvider(socialProvider);
                                        socialAccountDto.setSocialId(socialId);
                                        socialAccountDto.setEmail(email);
                                        socialAccountDto.setNickName("("+socialProvider+") "+nickname+"_"+socialId);
                                        socialAccountDto.setImageUrl(profileImg);

                                        socialSettingService.makeSocialTemp(socialAccountDto);
                                        return new NotFoundSocialAccount(socialAccountDto, "회원가입이 필요합니다. 회원가입 하시겠습니까?");
                                    });
                            socialSettingService.socialIdSet(userEntityEmail, socialId+3000000000000000000L);
                            return new ConflictException("소셜 이메일과 동일한 이메일로 가입된 계정이 있습니다. 소셜 계정과 연동 시키겠습니까?",
                                    String.valueOf(socialId+3000000000000000000L));
                        });
                break;
            case "NAVER":
                break;
            case "GOOGLE":
                break;
        }
        List<String> roles = userEntity.getUserRoles().stream()
                .map(u->u.getRoles()).map(r->r.getName()).toList();
        SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(userEntity);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.value(), "로그인에 성공 하였습니다.", signUpResponse);


        return Arrays.asList(jwtTokenConfig.createToken(userEntity.getEmail()), responseDto);
    }

    public ResponseEntity<ResponseDto> connectAccount(boolean isConnect, Long socialId) {
        UserEntity userEntity = userJpa.findBySocialId(socialId);
        if (userEntity==null) throw new NotFoundException("연결하거나 연결 취소할 계정을 찾을 수 없습니다.", socialId);

        SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(userEntity);

        if(isConnect) {
            socialSettingService.applySocialId(userEntity,socialId);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.CREATED.value(),
                    "소셜 연결이 완료 되었습니다.", signUpResponse),
                    HttpStatus.CREATED
            );
        }else {
            socialSettingService.cancelConnect(userEntity);

            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.ACCEPTED.value(),
                            "소셜 연결이 취소 되었습니다.",signUpResponse),
                    HttpStatus.ACCEPTED
            );
        }
    }


    public ResponseEntity<ResponseDto> socialSignUpFix(boolean isSignUp, Long socialId, SignUpRequest signUpRequest) {

        UserEntity userEntity = userJpa.findBySocialId(socialId);

        if(userEntity == null) throw new NotFoundException("가입중인 계정이 없습니다.", socialId.toString());
        else if(!userEntity.getStatus().equals("temp")) throw new ConflictException("알 수 없는 충돌 에러", socialId.toString());
        else if(!isSignUp) {
            socialSettingService.deleteSigningUpAccount(userEntity);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.ACCEPTED.value(),
                            "소셜 가입을 취소 하였습니다."),
                    HttpStatus.ACCEPTED
            );

        } else{
            String password = signUpRequest.getPassword();

            if(userJpa.existsByEmail(signUpRequest.getEmail())){
                throw new ConflictException("이미 입력하신 "+signUpRequest.getEmail()+" 이메일로 가입된 계정이 있습니다.",signUpRequest.getEmail());
            }else if (signUpRequest.getNickName().length()>30) {
                throw new BadRequestException("닉네임은 30자리 이하여야 합니다.", signUpRequest.getNickName());
            } else if(userJpa.existsByNickName(signUpRequest.getNickName())){
                throw new ConflictException("이미 입력하신 "+signUpRequest.getNickName()+" 닉네임으로 가입된 계정이 있습니다.",signUpRequest.getNickName());
            }
            else if(!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
                    ||!(password.length()>=8&&password.length()<=20)
            ){
                throw new BadRequestException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",password);
            } else if (!signUpRequest.getPasswordConfirm().equals(password)) {
                throw new BadRequestException("비밀번호와 비밀번호 확인이 같지 않습니다.","password : "+password+", password_confirm : "+signUpRequest.getPasswordConfirm());
            } else if (!(signUpRequest.getGender().equals("남성")||signUpRequest.getGender().equals("여성"))) {
                throw new BadRequestException("성별은 남성 또는 여성이어야 합니다.", signUpRequest.getGender());
            }


            socialSettingService.loadSigningUpAccount(userEntity, signUpRequest);
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.CREATED.value(),
                            userEntity.getNickName()+"님 소셜 가입이 완료 되었습니다."),
                    HttpStatus.CREATED
            );
        }
    }

}

