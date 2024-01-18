package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.service.authAccount.SignUpLoginService;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.account.LoginRequest;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpLoginController {
    private final SignUpLoginService signUpLoginService;
    @GetMapping("/test9")
    public String test(){
        throw new NotFoundException("1","2");
    }
    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignUpRequest signUpRequest){
        return signUpLoginService.signUp(signUpRequest);
    }
    @GetMapping("/sign-up")
    public String redirectToSignUpPage() {
        return "redirect:/untitled/Test/signup.html?_ijt=5tikatvivfra30akbkui2id0da&_ij_reload=RELOAD_ON_SAVE";
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
    @GetMapping("/oauth2/token")
    public String tokenTest3(){

        return "메롱";
    }

    @GetMapping("/login")
    public String logoutDirection(){
        return "로그인화면";
    }//임시 필요없음


}
