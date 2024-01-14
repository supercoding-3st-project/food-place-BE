package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class AccountLockedException extends RuntimeException{
    private final String request;

    public AccountLockedException(String message, String request) {
        super(message);
        this.request = request;
    }

//    public AccountLockedException( String name, String minute, String seconds){
//        super(String.format(
//                "\"%s\"님의 계정이 비밀번호 5회 실패로 잠겼습니다.\n" +
//                "남은 시간 : %s분 %s초", name, minute, seconds)
//        );
//    }
}
