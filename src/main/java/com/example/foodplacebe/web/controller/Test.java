package com.example.foodplacebe.web.controller;

import com.example.foodplacebe.service.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class Test {

    @GetMapping("/test")
    public String test() {
        throw new BadRequestException("error test", "error test request");
//        return "Test";
    }
}
