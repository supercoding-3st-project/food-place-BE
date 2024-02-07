package com.github.foodplacebe.service.mappers;

import com.github.foodplacebe.repository.userRoles.Roles;
import com.github.foodplacebe.repository.userRoles.UserRoles;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.account.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "joinDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "dateOfBirth", expression = "java(birthFormatting(signUpRequest.getDateOfBirth()))")
    @Mapping(target = "failureCount", expression = "java(0)")
    @Mapping(target = "status", expression = "java(\"normal\")")
    @Mapping(target = "imageUrl", expression = "java(defaultProfileImgSetting())")
    UserEntity signUpRequestToUserEntity(SignUpRequest signUpRequest);

    @Mapping(target = "profileImg", source = "imageUrl")
    SignUpResponse userEntityToSignUpResponse(UserEntity userEntity);

    AccountDto userEntityToAccountDTO(UserEntity userEntity);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    UserEntity AccountDTOToUserEntity(AccountDto accountDTO);

    UserEntity socialAccountDtoToUserEntity(SocialAccountDto socialAccountDto);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    UserEntity AccountPatchDtoToUserEntity(AccountPatchDto accountDTO);

    default String formatting(LocalDateTime localDateTime){
        if( localDateTime != null ){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 - HH시 mm분");
            return localDateTime.format(dateTimeFormatter);
        }else return null;
    }
    default LocalDate birthFormatting(String birth){
        if(birth != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(birth,formatter);
        }else return null;

    }
    default String defaultProfileImgSetting(){
        return "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg";
    }

    default List<String> rolesMapper(UserEntity userEntity){
        return userEntity.getUserRoles().stream()
                .map(ur->ur.getRoles()).map(r->r.getName()).toList();
    }


    default Collection<UserRoles> getRoles(List<Roles> roles) {
        return roles.stream()
                .map(role->{
                    UserRoles userRoles = new UserRoles();
                    userRoles.setRoles(role);
                    return userRoles;
                })
                .collect(Collectors.toList());
    }
}
