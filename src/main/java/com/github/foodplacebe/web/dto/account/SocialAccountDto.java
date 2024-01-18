package com.github.foodplacebe.web.dto.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SocialAccountDto {
    private Long socialId;
    private String provider;
    private String name;
    private String email;
    private String imageUrl;
    private String gender;
    private String dateOfBirth;
}
