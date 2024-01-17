package com.github.foodplacebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FoodPlaceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodPlaceBeApplication.class, args);
    }

}
