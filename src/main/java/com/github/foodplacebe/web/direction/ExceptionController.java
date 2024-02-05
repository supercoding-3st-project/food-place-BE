package com.github.foodplacebe.web.direction;

import com.github.foodplacebe.service.exceptions.AccessDenied;
import com.github.foodplacebe.service.exceptions.NotAcceptableException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping(value = "/exceptions")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException(@RequestParam(name = "token", required = false) String token) {
        if (token==null) throw new NotAcceptableException("로그인이 필요합니다.", null);
        else throw new NotAcceptableException("로그인이 만료 되었습니다.","유효하지 않은 토큰 : "+ token);
    }

    @GetMapping(value = "/access-denied")
    public void accessDeniedException(@RequestParam(name = "roles", required = false) String roles) {
        if(roles==null) throw new AccessDenied("권한이 설정되지 않았습니다.",null);
        else throw new AccessDenied("권한이 없습니다.", "시도한 유저의 권한 : "+roles);
    }
}