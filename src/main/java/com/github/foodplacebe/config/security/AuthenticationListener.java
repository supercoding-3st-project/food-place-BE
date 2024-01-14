package com.github.foodplacebe.config.security;

import com.github.foodplacebe.service.authAccount.AccountLockService;
import com.github.foodplacebe.service.exceptions.AccountLockedException;
import com.github.foodplacebe.service.exceptions.CustomBadCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationListener {
    private final AccountLockService accountLockService;

    @EventListener
    public void handleBadCredentials(AuthenticationFailureBadCredentialsEvent event){
        Map<String, String> result = accountLockService.failureCount(event);
        String wrongPassword = (String)event.getAuthentication().getCredentials();
        if(result.get("request")!=null){
            if(result.get("request").equals("unlock")){
                accountLockService.resetFailureCount(event.getAuthentication().getName());
                result = accountLockService.failureCount(event);
                throw new CustomBadCredentialsException("비밀번호가 틀립니다. "+(5-Integer.parseInt(result.get("remaining")))
                        +"번 틀렸습니다. "+"남은 횟수 : "+result.get("remaining"),wrongPassword);
            }else if(result.get("request").equals("increment")) {
                if (result.get("remaining").equals("1")){
                    throw new CustomBadCredentialsException("비밀번호가 틀립니다. "+(5-Integer.parseInt(result.get("remaining")))
                            +"번 틀렸습니다. "+"남은 횟수 : "+result.get("remaining")
                            +", 한번 더 로그인 실패시 "+event.getAuthentication().getName()
                            +" 계정이 5분간 잠깁니다.", wrongPassword);
                }
                throw new CustomBadCredentialsException("비밀번호가 틀립니다. "+(5-Integer.parseInt(result.get("remaining")))
                        +"번 틀렸습니다. "+"남은 횟수 : "+result.get("remaining"),wrongPassword);
            } else if(result.get("request").equals("locked")){
//                throw new AccountLockedException(result.get("name"),result.get("minute"),result.get("seconds"));
                throw new AccountLockedException(
                        String.format(
                        "\"%s\"님의 계정이 비밀번호 5회 실패로 잠겼습니다. 남은 시간 : %s분 %s초", result.get("name"),result.get("minute"),result.get("seconds")
                        ), wrongPassword);
            }
        }
    }
    @EventListener
    public void handleSuccessEvent(AuthenticationSuccessEvent event){
        String email = event.getAuthentication().getName();
        accountLockService.resetFailureCount(email);
    }
}
