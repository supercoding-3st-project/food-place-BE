package com.example.foodplacebe.web.controller;


import com.example.foodplacebe.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class Test {

    @GetMapping("/test")
    public String test() {
        throw new NotFoundException("error test", "error test request");
    }
}
