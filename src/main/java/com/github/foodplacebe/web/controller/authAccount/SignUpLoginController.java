package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.service.authAccount.SignUpLoginService;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.account.LoginRequest;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpLoginController {
    private final SignUpLoginService signUpLoginService;

    @GetMapping("/test8")
    public ResponseEntity<ResponseDto> test0(){
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.ACCEPTED.value(),
                        "소셜 가입을 취소 하였습니다."),
                HttpStatus.ACCEPTED
        );
    }
    @GetMapping("/test9")
    public ResponseEntity<ResponseDto> test(){
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.ACCEPTED.value(),
                        "소셜 가입을 취소 하였습니다."),
                HttpStatus.ACCEPTED
        );
    }
    @GetMapping("/test10")
    public ResponseEntity<ResponseDto> test1(){
        return new ResponseEntity<>(
                new ResponseDto(HttpStatus.CREATED.value(),
                        "소셜 가입을 취소 하였습니다."),
                HttpStatus.CREATED
        );
    }
    @PostMapping("/sign-up")
    public ResponseDto signUp(@RequestBody SignUpRequest signUpRequest){
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
    public ResponseDto login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        List<Object> tokenAndResponse = signUpLoginService.login(loginRequest);
        httpServletResponse.setHeader("Token", (String) tokenAndResponse.get(0));
        return (ResponseDto) tokenAndResponse.get(1);
    }

    @GetMapping("/login")
    public String logoutDirection(){
        return "로그인화면";
    }//임시 필요없음


}
