package com.github.foodplacebe.web.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyInfoRequest {
    private String nickName;
    private String phoneNum;
    private String neighborhood;
    private String gender;
    private String dateOfBirth;
    private String password;
}
