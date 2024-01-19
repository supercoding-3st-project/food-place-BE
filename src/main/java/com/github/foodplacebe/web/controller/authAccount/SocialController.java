package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.account.oauth.client.KakaoLoginParams;
import com.github.foodplacebe.web.dto.account.oauth.client.NaverLoginParams;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.oauth.SocialSignUpService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/social")
@RequiredArgsConstructor
public class SocialController {
    private final SocialSignUpService socialSignUpService;
    @GetMapping("/kakao")
    public String kakaoLogin (@RequestParam final String code){
        return code;
    }
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/success")
    public String success(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String cu = customUserDetails.getUsername();
        return "success";
    }
    @PostMapping("/kakao")
    public ResponseEntity<ResponseDto> loginKakao(@RequestBody KakaoLoginParams params, HttpServletResponse httpServletResponse) {

        List<String> resList = socialSignUpService.login(params);
        httpServletResponse.setHeader("Token", resList.get(0));
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.OK.value(), resList.get(1)+"님 환영합니다."),
                HttpStatus.OK
        );
    }
    @PostMapping("/connect")
    public ResponseEntity<ResponseDto> connectAccount(
            @RequestParam(name = "is-connect") boolean isConnect,
            @RequestParam(name = "social-id") Long socialId){
        return socialSignUpService.connectAccount(isConnect, socialId);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> socialSignUp(
            @RequestParam(name = "is-sign-up") boolean isSignUp,
            @RequestParam(name = "social-id") Long socialId,
            @RequestBody SignUpRequest signUpRequest){
        return socialSignUpService.socialSignUpFix(isSignUp, socialId, signUpRequest);
    }
//    @PostMapping("/naver")
//    public String loginNaver(@RequestBody NaverLoginParams params) {
//        return socialSignUpService.login(params);
//    }
}
