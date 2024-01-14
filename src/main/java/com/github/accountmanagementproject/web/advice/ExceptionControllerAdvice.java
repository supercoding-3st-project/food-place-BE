package com.github.accountmanagementproject.web.advice;
import com.github.accountmanagementproject.service.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {



    @ExceptionHandler(AccessDenied.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) //권한이 없을때
    public Map<String, Object> handleNotAccessDenied(AccessDenied ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.FORBIDDEN.value());
        response.put("message", HttpStatus.FORBIDDEN.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(AccountLockedException.class)
    @ResponseStatus(HttpStatus.LOCKED) //계정이 잠겼을때
    public Map<String, Object> handleAccountLockedException(AccountLockedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.LOCKED.value());
        response.put("message", HttpStatus.LOCKED.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT) //입력한 키가 이미 존재할때
    public Map<String, Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.CONFLICT.value());
        response.put("message", HttpStatus.CONFLICT.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(NotAcceptException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) //처리할 수 없는 요청
    public Map<String, Object> handleNotAcceptException(NotAcceptException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_ACCEPTABLE.value());
        response.put("message", HttpStatus.NOT_ACCEPTABLE.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //찾을 수 없을때
    public Map<String, Object> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("message", HttpStatus.NOT_FOUND.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(CustomBindException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //양식과 맞지 않을때
    public Map<String, Object> handleCustomBindException(CustomBindException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("message", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    @ExceptionHandler(CustomBadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //비밀번호가 틀렸을때
    public Map<String, Object> handleBadCredentialsException(CustomBadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.UNAUTHORIZED.value());
        response.put("message", HttpStatus.UNAUTHORIZED.toString());
        response.put("detailMessage", ex.getMessage());
        response.put("request", ex.getRequest());
        return response;
    }

    // 다른 예외에 대한 핸들러 메소드들을 추가할 수 있습니다.
}

