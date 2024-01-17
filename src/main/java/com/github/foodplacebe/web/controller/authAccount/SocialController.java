package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.web.dto.account.oauth.client.KakaoLoginParams;
import com.github.foodplacebe.web.dto.account.oauth.client.NaverLoginParams;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.SocialSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public String loginKakao(@RequestBody KakaoLoginParams params) {
        return socialSignUpService.login(params);
    }

    @PostMapping("/naver")
    public String loginNaver(@RequestBody NaverLoginParams params) {
        return socialSignUpService.login(params);
    }
}
