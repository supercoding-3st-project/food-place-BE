package com.github.foodplacebe.web.dto.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountDto {
    private String name;
    private String email;
    private String nickName;
    private String phoneNumber;
    private String imageUrl;
    private String neighborhood;
    private String gender;
    private String joinDate;
}
