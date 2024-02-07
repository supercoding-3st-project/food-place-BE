package com.github.foodplacebe.web.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpResponse {
    private Integer userId;
    private String profileImg;
    private String nickName;
}
