package com.github.foodplacebe.web.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class UpdatePasswordRequest {
    private String password;
    private String updatePassword;
    private String updatePasswordConfirm;
}
