package com.github.foodplacebe.service.security;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJpa userJpa;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userJpa.findByEmailJoin(email).orElseThrow(() ->
                new NotFoundException("(토큰에러) 해당 이메일을 찾을 수 없습니다.", email));

        return CustomUserDetails.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .nickName(userEntity.getNickName())
                .name(userEntity.getName())

                .authorities(userEntity.getUserRoles()
                        .stream().map(ur->ur.getRoles())
                        .map(r->r.getName())
                        .collect(Collectors.toList()))
                .build();

    }

}
