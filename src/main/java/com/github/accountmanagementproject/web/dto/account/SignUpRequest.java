package com.github.accountmanagementproject.web.dto.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
    private String email;
    private String phoneNumber;
    private String password;
    private String passwordConfirm;
    private String name;
    private String nickName;
    private String gender;
    private String dateOfBirth;
    private String neighborhood;
    private String imageUrl;
}
