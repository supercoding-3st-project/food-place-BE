package com.github.accountmanagementproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AccountManagementProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountManagementProjectApplication.class, args);
    }

}
