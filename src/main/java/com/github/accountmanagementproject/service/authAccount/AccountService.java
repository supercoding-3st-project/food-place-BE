package com.github.accountmanagementproject.service.authAccount;

import com.github.accountmanagementproject.repository.userDetails.CustomUserDetails;
import com.github.accountmanagementproject.repository.userRoles.RolesJpa;
import com.github.accountmanagementproject.repository.userRoles.UserRolesJpa;
import com.github.accountmanagementproject.repository.users.UserEntity;
import com.github.accountmanagementproject.repository.users.UserJpa;
import com.github.accountmanagementproject.service.customExceptions.CustomBindException;
import com.github.accountmanagementproject.service.customExceptions.DuplicateKeyException;
import com.github.accountmanagementproject.service.customExceptions.NotFoundException;
import com.github.accountmanagementproject.service.mappers.UserMapper;
import com.github.accountmanagementproject.web.dto.account.AccountDto;
import com.github.accountmanagementproject.web.dto.account.AccountPatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserJpa userJpa;
    private final UserRolesJpa userRolesJpa;
    private final RolesJpa rolesJpa;
    private final PasswordEncoder passwordEncoder;





    public AccountDto getMyInfo(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findByEmailJoin(customUserDetails.getUsername())
                .orElseThrow(()->new NotFoundException("계정 정보를 찾을 수 없습니다.",customUserDetails.getUsername()));
        List<String> roles = userEntity.getUserRoles().stream()
                .map(ur->ur.getRoles()).map(r->r.getName()).toList();
        return UserMapper.INSTANCE.userEntityToAccountDTO(userEntity);
    }

    @Transactional(transactionManager = "tm")
    public AccountDto patchMyInfo(CustomUserDetails customUserDetails, AccountPatchDto accountDTO) {
        String loggedEmail = customUserDetails.getUsername();
        String email = accountDTO.getEmail();
        String phoneNumber = accountDTO.getPhoneNumber();
        String nickName = accountDTO.getNickName();

        if(!email.matches(".+@.+\\..+")){
            throw new CustomBindException("이메일을 정확히 입력해주세요.",email);
        } else if (!phoneNumber.matches("01\\d{9}")) {
            throw new CustomBindException("핸드폰 번호를 확인해주세요.", phoneNumber);
        } else if (nickName.matches("01\\d{9}")){
            throw new CustomBindException("핸드폰 번호를 닉네임으로 사용할수 없습니다.",nickName);
        }

        if(userJpa.existsByEmailAndEmailNot(email, loggedEmail)){
            throw new DuplicateKeyException("이미 입력하신 "+email+" 이메일로 가입된 계정이 있습니다.",email);
        }else if(userJpa.existsByPhoneNumberAndEmailNot(phoneNumber, loggedEmail)) {
            throw new DuplicateKeyException("이미 입력하신 "+phoneNumber+" 핸드폰 번호로 가입된 계정이 있습니다.",phoneNumber);
        }else if(userJpa.existsByNickNameAndEmailNot(nickName, loggedEmail)){
            throw new DuplicateKeyException("이미 입력하신 "+nickName+" 닉네임으로 가입된 계정이 있습니다.",nickName);
        }


        UserEntity existingUser = userJpa.findByEmail(customUserDetails.getUsername());
//        List<Roles> roles = accountDTO.getUserRoles().stream()
//                .map(rn->rolesJpa.findByName(rn)).toList();//롤변경시 필요
        UserEntity updateUser = UserMapper.INSTANCE.AccountPatchDtoToUserEntity(accountDTO);

        if(accountDTO.getPassword()==null) throw new CustomBindException("현재 비밀번호를 입력해 주세요.","");


        if(passwordEncoder.matches(accountDTO.getPassword(), customUserDetails.getPassword())){
            if (accountDTO.getNewPassword()!=null||accountDTO.getNewPasswordConfirm()!=null) {
                if(accountDTO.getNewPassword()==null||accountDTO.getNewPasswordConfirm()==null) throw new CustomBindException(
                        "비밀번호 변경을 원하시면 새로운 비밀번호와, 비밀번호 확인 둘다 입력해주세요.",
                        "new_password : "+accountDTO.getNewPassword()+", new_password_confirm : "+accountDTO.getNewPasswordConfirm());
                else if (accountDTO.getPassword().equals(accountDTO.getNewPassword())) throw  new CustomBindException("현재 비밀번호와 변경하려는 비밀번호가 같습니다.",
                        "password : "+accountDTO.getPassword()+", new_password : "+accountDTO.getNewPassword());
                else if(accountDTO.getNewPassword().equals(accountDTO.getNewPasswordConfirm())){
                    if(!accountDTO.getNewPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
                            ||!(accountDTO.getNewPassword().length()>=8&&accountDTO.getNewPassword().length()<=20)
                    ){
                        throw new CustomBindException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",accountDTO.getNewPassword());
                    }

                    updateUser.setPassword(passwordEncoder.encode(accountDTO.getNewPassword()));
                }
            }else updateUser.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

            BeanUtils.copyProperties(updateUser, existingUser, getNullPropertyNames(updateUser));
        }else throw new CustomBindException("현재 비밀번호와 입력하신 비밀번호가 틀립니다.",accountDTO.getPassword());

        List<String> roles = existingUser.getUserRoles().stream()
                .map(ur->ur.getRoles()).map(r->r.getName()).toList();
        return UserMapper.INSTANCE.userEntityToAccountDTO(existingUser);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Stream.of(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(pn->src.getPropertyValue(pn) == null)
                .toArray(String[]::new);
    }




    //회원 탈퇴
    @Transactional(transactionManager = "tm")
    public String withdrawal(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId()).orElseThrow(
                ()->new NotFoundException("계정을 찾을 수 없습니다. 다시 로그인 해주세요.", customUserDetails.getUsername())
        );
        if(userEntity.getStatus().equals("delete")){
            throw new CustomBindException("이미 탈퇴처리된 회원 입니다.", customUserDetails.getUsername());
        }
        userEntity.setStatus("delete");
        userEntity.setDeletionDate(LocalDateTime.now());

        return userEntity.getEmail()+" 계정 회원 탈퇴 완료";
    }
}
