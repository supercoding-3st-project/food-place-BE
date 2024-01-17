package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.SocialSignUpService;
import com.github.foodplacebe.service.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
public class SocialController {
    private final SocialSignUpService socialSignUpService;
//    @GetMapping("/kakao")
//    public String kakaoLogin (@RequestParam final String code){
//        return socialSignUpService.getToken(code);
//    }
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/success")
    public String success(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String cu = customUserDetails.getUsername();
        return "success";
    }
}
