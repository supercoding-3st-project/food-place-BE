package com.github.accountmanagementproject.web.direction;

import com.github.accountmanagementproject.service.customExceptions.AccessDenied;
import com.github.accountmanagementproject.service.customExceptions.NotAcceptException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exceptions")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new NotAcceptException("로그인이 필요합니다.","");
    }

    @GetMapping(value = "/access-denied")
    public void accessDeniedException() {
        throw new AccessDenied("권한이 없습니다.","");
    }
}