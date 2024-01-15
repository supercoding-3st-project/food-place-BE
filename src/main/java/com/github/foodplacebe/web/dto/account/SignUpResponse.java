package com.github.foodplacebe.web.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpResponse {
    private String name;
    private String joinDate;
}
