package com.github.foodplacebe.service.authAccount;

import com.github.foodplacebe.config.security.JwtTokenConfig;
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
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignUpLoginService {
    private final UserJpa userJpa;
    private final RolesJpa rolesJpa;
    private final UserRolesJpa userRolesJpa;
    private final JwtTokenConfig jwtTokenConfig;


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    //회원가입 로직
    @Transactional(transactionManager = "tm")
    public ResponseDto signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
//        String phoneNumber = signUpRequest.getPhoneNumber();
        String password = signUpRequest.getPassword();


        if(!email.matches(".+@.+\\..+")){
            throw new BadRequestException("이메일을 정확히 입력해주세요.",email);
//        } else if (!phoneNumber.matches("01\\d{9}")) {
//            throw new BadRequestException("핸드폰 번호를 확인해주세요.", phoneNumber);
        } else if (signUpRequest.getNickName().matches("01\\d{9}")){
            throw new BadRequestException("핸드폰 번호를 닉네임으로 사용할수 없습니다.",signUpRequest.getNickName());
        }

        if(userJpa.existsByEmail(signUpRequest.getEmail())){
            throw new ConflictException("이미 입력하신 "+signUpRequest.getEmail()+" 이메일로 가입된 계정이 있습니다.",signUpRequest.getEmail());
//        }else if(userJpa.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            throw new ConflictException("이미 입력하신 "+signUpRequest.getPhoneNumber()+" 핸드폰 번호로 가입된 계정이 있습니다.",signUpRequest.getPhoneNumber());
        }else if(userJpa.existsByNickName(signUpRequest.getNickName())){
            throw new ConflictException("이미 입력하신 "+signUpRequest.getNickName()+" 닉네임으로 가입된 계정이 있습니다.",signUpRequest.getNickName());
        }
        else if(!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
                ||!(password.length()>=8&&password.length()<=20)
        ){
            throw new BadRequestException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",password);
        } else if (!signUpRequest.getPasswordConfirm().equals(password)) {
            throw new BadRequestException("비밀번호와 비밀번호 확인이 같지 않습니다.","password : "+password+", password_confirm : "+signUpRequest.getPasswordConfirm());
        }



        signUpRequest.setPassword(passwordEncoder.encode(password));

        Roles roles = rolesJpa.findByName("ROLE_USER");


        if(!(signUpRequest.getGender().equals("남성")||signUpRequest.getGender().equals("여성"))) throw new BadRequestException("성별은 남성 혹은 여성 이어야 합니다.", signUpRequest.getGender());

        UserEntity userEntity = UserMapper.INSTANCE.signUpRequestToUserEntity(signUpRequest);
        userJpa.save(userEntity);

        userRolesJpa.save(UserRoles.builder()
                        .userEntity(userEntity)
                        .roles(roles)
                .build());

        SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(userEntity);

        return new ResponseDto(HttpStatus.OK.value(), userEntity.getNickName()+"님 회원 가입이 완료 되었습니다.", signUpResponse);
    }


    //로그인 로직
    public List<Object> login(LoginRequest loginRequest){
        String requestEmail = loginRequest.getEmail();
        UserEntity userEntity;

//        if(emailOrPhoneNumber.matches("01\\d+")&&emailOrPhoneNumber.length()==11){
//            userEntity = userJpa.findByPhoneNumberJoin(emailOrPhoneNumber).orElseThrow(()->
//                    new NotFoundException("입력하신 핸드폰 번호의 계정을 찾을 수 없습니다.", emailOrPhoneNumber)
//                    );
//        } else if (emailOrPhoneNumber.matches(".+@.+\\..+")) {
//            userEntity = userJpa.findByEmailJoin(emailOrPhoneNumber).orElseThrow(()->
//                    new NotFoundException("입력하신 이메일의 계정을 찾을 수 없습니다.", emailOrPhoneNumber)
//            );
//        } else userEntity = userJpa.findByNickNameJoin(emailOrPhoneNumber).orElseThrow(()->
//                    new NotFoundException("입력하신 닉네임의 계정을 찾을 수 없습니다.", emailOrPhoneNumber)
//        );
        if (requestEmail.matches(".+@.+\\..+")) {
            userEntity = userJpa.findByEmailJoin(requestEmail).orElseThrow(()->
                    new NotFoundException("입력하신 이메일의 계정을 찾을 수 없습니다.", requestEmail)
            );
        } else throw new BadRequestException("이메일이 잘못 입력되었습니다.", requestEmail);

        try{
            if(userEntity.getStatus().equals("delete")){
                throw new AccessDenied("탈퇴한 계정입니다.",requestEmail);
            }

//            String t1 = userEntity.getPassword();
//            String t2 = passwordEncoder.encode(loginRequest.getPassword());
//            if(passwordEncoder.matches(loginRequest.getPassword(), t1)){
//                System.out.println("a");
//            }

            if(userEntity.getStatus().equals("lock")){
                LocalDateTime lockDateTime = userEntity.getLockDate();
                LocalDateTime now = LocalDateTime.now();
                if(now.isBefore(lockDateTime.plusMinutes(5))){
                    Duration duration = Duration.between(now, lockDateTime.plusMinutes(5));
                    String minute = String.valueOf(duration.toMinutes());
                    String seconds = String.valueOf(duration.minusMinutes(duration.toMinutes()).getSeconds());
                    throw new AccountLockedException(String.format(
                            "'%s'님의 계정이 비밀번호 5회 실패로 잠겼습니다. 남은 시간 : %s분 %s초", userEntity.getNickName(),minute,seconds
                    ), loginRequest.getPassword());
                }
            }
//            String email = userEntity.getEmail();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestEmail, loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            List<String> roles = userEntity.getUserRoles().stream()
                    .map(u->u.getRoles()).map(r->r.getName()).toList();
            SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(userEntity);
            ResponseDto responseDto = new ResponseDto(HttpStatus.OK.value(), "로그인에 성공 하였습니다.", signUpResponse);

            return Arrays.asList(jwtTokenConfig.createToken(requestEmail, roles), responseDto);
//        }catch (InternalAuthenticationServiceException e){
//            throw new NotFoundException(String.format("해당 이메일 또는 핸드폰번호 \"%s\"의 계정을 찾을 수 없습니다.", emailOrPhoneNumber));
        }
        catch (BadCredentialsException e){
            throw new CustomBadCredentialsException("비밀번호가 틀립니다. "+e.getMessage(),loginRequest.getPassword());
        }
    }

    public boolean checkEmail(String email) {
        if (!email.matches(".+@.+\\..+")) {
            throw new BadRequestException("이메일을 정확히 입력해주세요.",email);
        }
        return !userJpa.existsByEmail(email);
    }
}
