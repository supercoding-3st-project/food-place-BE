package com.github.accountmanagementproject.web.controller.authAccount;

import com.github.accountmanagementproject.service.authAccount.SignUpLoginService;
import com.github.accountmanagementproject.service.authAccount.SocialSignUpService;
import com.github.accountmanagementproject.web.dto.account.LoginRequest;
import com.github.accountmanagementproject.web.dto.account.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpLoginController {
    private final SignUpLoginService signUpLoginService;
    private final SocialSignUpService socialSignUpService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @GetMapping("/test9")
    public String test(){
        return "성공";
    }
    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignUpRequest signUpRequest){
        return signUpLoginService.signUp(signUpRequest);
    }

    @GetMapping("/sign-up/check-email")
    public boolean checkEmail(HttpServletRequest httpServletRequest){
        return signUpLoginService.checkEmail(httpServletRequest.getParameter("email"));
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        List<String> tokenAndUserName = signUpLoginService.login(loginRequest);
        httpServletResponse.setHeader("Token", tokenAndUserName.get(0));
        return "\""+tokenAndUserName.get(1)+"\"님 환영합니다.";
    }
    @GetMapping("/oauth2/code")
    public String tokenTest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("oauth-token",httpServletRequest.getParameter("code"));
        return httpServletRequest.getParameter("code");
    }
    @PostMapping("/oauth2/token")
    public String tokenTest2(@RequestBody Map<String, String> requestMap, Authentication authentication){
        String accessToken = requestMap.get("accessToken");
        System.out.println("Received Access Token: " + accessToken);

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("User ID: " + authentication.getName());
        }
        return "메롱";
    }
    @GetMapping("/oauth2/token")
    public String tokenTest3(){

        return "메롱";
    }

    @GetMapping("/login")
    public String logoutDirection(){
        return "로그인화면";
    }//임시 필요없음


}
