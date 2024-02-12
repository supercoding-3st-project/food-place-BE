package com.github.foodplacebe.config;

import com.github.foodplacebe.repository.userRoles.Roles;
import com.github.foodplacebe.repository.userRoles.RolesJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserSettingConfig {
    private final RolesJpa rolesJpa;
    @Bean
    public Roles getNormalUserRole(){
        return rolesJpa.findByName("ROLE_USER");
    }

}
