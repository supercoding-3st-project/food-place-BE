package com.github.foodplacebe.service.authAccount.oauth;

import com.github.foodplacebe.repository.userRoles.Roles;
import com.github.foodplacebe.repository.userRoles.RolesJpa;
import com.github.foodplacebe.repository.userRoles.UserRoles;
import com.github.foodplacebe.repository.userRoles.UserRolesJpa;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotAcceptableException;
import com.github.foodplacebe.service.mappers.UserMapper;
import com.github.foodplacebe.web.dto.account.AccountDto;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.account.SocialAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SocialSettingService {
    private final UserJpa userJpa;
    private final RolesJpa rolesJpa;
    private final UserRolesJpa userRolesJpa;

    @Transactional(transactionManager = "tm")
    public void socialIdSet(UserEntity userEntity, Long socialId) {
        userEntity.setSocialId(socialId);
    }

    @Transactional(transactionManager = "tm")
    public void makeSocialTemp(SocialAccountDto socialAccountDto) {
        UserEntity userEntity = UserMapper.INSTANCE.socialAccountDtoToUserEntity(socialAccountDto);
        //email name imageUrl 무시
        userEntity.setEmail("("+socialAccountDto.getProvider()+") "+userEntity.getEmail());
        userEntity.setSocialId(socialAccountDto.getSocialId());
        userEntity.setPhoneNumber(socialAccountDto.getSocialId().toString());
        userEntity.setPassword("Non-Alcoholic");
        userEntity.setGender(UserEntity.Gender.불명);
        userEntity.setJoinDate(LocalDateTime.now());
        userEntity.setDateOfBirth(LocalDate.now());
        userEntity.setStatus("temp");
        userEntity.setDeletionDate(LocalDateTime.now().minusDays(6));
        userEntity.setFailureCount(0);
        userJpa.save(userEntity);
    }

    @Transactional(transactionManager = "tm")
    public void cancelConnect(Long socialId) {
        UserEntity userEntity = userJpa.findBySocialId(socialId);
        userEntity.setSocialId(null);
    }

    @Transactional(transactionManager = "tm")
    public void deleteSigningUpAccount(UserEntity userEntity) {
        try{
            userJpa.delete(userEntity);
        } catch (Exception e){
            throw new NotAcceptableException("소셜 가입 취소 실패", userEntity.getSocialId().toString());
        }
    }

    @Transactional(transactionManager = "tm")
    public void loadSigningUpAccount(UserEntity existingUser, SignUpRequest signUpRequest) {
        UserEntity updateUser = UserMapper.INSTANCE.signUpRequestToUserEntity(signUpRequest);
        updateUser.setImageUrl(null);
        BeanUtils.copyProperties(updateUser, existingUser, getNullPropertyNames(updateUser));
        Roles roles = rolesJpa.findByName("ROLE_USER");
        userRolesJpa.save(UserRoles.builder()
                .userEntity(existingUser)
                .roles(roles)
                .build());
    }
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Stream.of(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(pn->src.getPropertyValue(pn) == null)
                .toArray(String[]::new);
    }
}
