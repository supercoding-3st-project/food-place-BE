package com.github.foodplacebe.service.authAccount;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.PostsService.PostPhotosService;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.ConflictException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.mappers.UserMapper;
import com.github.foodplacebe.web.dto.account.AccountDto;
import com.github.foodplacebe.web.dto.account.AccountPatchDto;
import com.github.foodplacebe.web.dto.account.UpdateMyInfoRequest;
import com.github.foodplacebe.web.dto.account.UpdatePasswordRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserJpa userJpa;
    private final PasswordEncoder passwordEncoder;
    private final PostPhotosService postPhotosService;

    private static final String PHONE_NUMBER_REGEX = "^01[0-9]{9}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // 정규식에 맞는지 확인
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }


    public ResponseDto getMyInfo(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findByEmailJoin(customUserDetails.getUsername())
                .orElseThrow(()->new NotFoundException("계정 정보를 찾을 수 없습니다.",customUserDetails.getUsername()));
//        List<String> roles = userEntity.getUserRoles().stream()
//                .map(ur->ur.getRoles()).map(r->r.getName()).toList();
        AccountDto accountDto = UserMapper.INSTANCE.userEntityToAccountDTO(userEntity);
        return new ResponseDto(HttpStatus.OK.value(), "수정될 회원의 정보를 가져오는데 성공 했습니다.", accountDto);
    }

    @Transactional(transactionManager = "tm")
    public AccountDto patchMyInfo(CustomUserDetails customUserDetails, AccountPatchDto accountDTO) {
        String loggedEmail = customUserDetails.getUsername();
        String email = accountDTO.getEmail();
        String phoneNumber = accountDTO.getPhoneNumber();
        String nickName = accountDTO.getNickName();

        if(!email.matches(".+@.+\\..+")){
            throw new BadRequestException("이메일을 정확히 입력해주세요.",email);
        } else if (!phoneNumber.matches("01\\d{9}")) {
            throw new BadRequestException("핸드폰 번호를 확인해주세요.", phoneNumber);
        } else if (nickName.matches("01\\d{9}")){
            throw new BadRequestException("핸드폰 번호를 닉네임으로 사용할수 없습니다.",nickName);
        }

        if(userJpa.existsByEmailAndEmailNot(email, loggedEmail)){
            throw new ConflictException("이미 입력하신 "+email+" 이메일로 가입된 계정이 있습니다.",email);
        }else if(userJpa.existsByPhoneNumberAndEmailNot(phoneNumber, loggedEmail)) {
            throw new ConflictException("이미 입력하신 "+phoneNumber+" 핸드폰 번호로 가입된 계정이 있습니다.",phoneNumber);
        }else if(userJpa.existsByNickNameAndEmailNot(nickName, loggedEmail)){
            throw new ConflictException("이미 입력하신 "+nickName+" 닉네임으로 가입된 계정이 있습니다.",nickName);
        }


        UserEntity existingUser = userJpa.findByEmail(customUserDetails.getUsername());
//        List<Roles> roles = accountDTO.getUserRoles().stream()
//                .map(rn->rolesJpa.findByName(rn)).toList();//롤변경시 필요
        UserEntity updateUser = UserMapper.INSTANCE.AccountPatchDtoToUserEntity(accountDTO);

        if(accountDTO.getPassword()==null) throw new BadRequestException("현재 비밀번호를 입력해 주세요.","");


        if(passwordEncoder.matches(accountDTO.getPassword(), customUserDetails.getPassword())){
            if (accountDTO.getNewPassword()!=null||accountDTO.getNewPasswordConfirm()!=null) {
                if(accountDTO.getNewPassword()==null||accountDTO.getNewPasswordConfirm()==null) throw new BadRequestException(
                        "비밀번호 변경을 원하시면 새로운 비밀번호와, 비밀번호 확인 둘다 입력해주세요.",
                        "new_password : "+accountDTO.getNewPassword()+", new_password_confirm : "+accountDTO.getNewPasswordConfirm());
                else if (accountDTO.getPassword().equals(accountDTO.getNewPassword())) throw  new BadRequestException("현재 비밀번호와 변경하려는 비밀번호가 같습니다.",
                        "password : "+accountDTO.getPassword()+", new_password : "+accountDTO.getNewPassword());
                else if(accountDTO.getNewPassword().equals(accountDTO.getNewPasswordConfirm())){
                    if(!accountDTO.getNewPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
                            ||!(accountDTO.getNewPassword().length()>=8&&accountDTO.getNewPassword().length()<=20)
                    ){
                        throw new BadRequestException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",accountDTO.getNewPassword());
                    }

                    updateUser.setPassword(passwordEncoder.encode(accountDTO.getNewPassword()));
                }
            }else updateUser.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

            BeanUtils.copyProperties(updateUser, existingUser, getNullPropertyNames(updateUser));
        }else throw new BadRequestException("현재 비밀번호와 입력하신 비밀번호가 틀립니다.",accountDTO.getPassword());

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
    public ResponseDto withdrawal(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId()).orElseThrow(
                ()->new NotFoundException("계정을 찾을 수 없습니다. 다시 로그인 해주세요.", customUserDetails.getUserId())
        );
        if(userEntity.getStatus().equals("delete")){
            throw new BadRequestException("이미 탈퇴처리된 회원 입니다.", customUserDetails.getUserId());
        }
        userEntity.setStatus("delete");
        userEntity.setDeletionDate(LocalDateTime.now());

        return new ResponseDto(200, "회원탈퇴 완료", userEntity.getNickName());
    }

    public ResponseDto getAccountInfo(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findByEmailJoin(customUserDetails.getUsername())
                .orElseThrow(()->new NotFoundException("계정 정보를 찾을 수 없습니다.",customUserDetails.getUsername()));

        AccountDto accountDto = UserMapper.INSTANCE.userEntityToAccountDTO(userEntity);
        return new ResponseDto(HttpStatus.OK.value(), "회원정보 조회 성공.", accountDto);
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto updateMyInfo(CustomUserDetails customUserDetails, UpdateMyInfoRequest updateMyInfoRequest, List<MultipartFile> multipartFiles) {
        UserEntity user = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("user 정보를 찾을 수 없습니다", customUserDetails.getUserId()));

        if (!(passwordEncoder.matches(updateMyInfoRequest.getPassword(), user.getPassword())))
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", "");
        else if (updateMyInfoRequest.getPhoneNum() != null && !updateMyInfoRequest.getPhoneNum().isEmpty() && !isValidPhoneNumber(updateMyInfoRequest.getPhoneNum())) {
            throw new BadRequestException("핸드폰 번호를 확인해주세요.", updateMyInfoRequest.getPhoneNum());
        } else if (isValidPhoneNumber(updateMyInfoRequest.getNickName())){
            throw new BadRequestException("핸드폰 번호를 닉네임으로 사용할수 없습니다.",updateMyInfoRequest.getNickName());
        }



        if (multipartFiles != null && !multipartFiles.isEmpty()){
            String profileImg = postPhotosService.uploadProfileImg(user.getNickName(), multipartFiles.get(0));
            user.setImageUrl(profileImg);
        }

        if (updateMyInfoRequest.getNickName() != null && !updateMyInfoRequest.getNickName().isEmpty())
            user.setNickName(updateMyInfoRequest.getNickName());

        if (updateMyInfoRequest.getPhoneNum() != null && !updateMyInfoRequest.getPhoneNum().isEmpty())
            user.setPhoneNumber(updateMyInfoRequest.getPhoneNum());

        if (updateMyInfoRequest.getNeighborhood() != null && !updateMyInfoRequest.getNeighborhood().isEmpty())
            user.setNeighborhood(updateMyInfoRequest.getNeighborhood());

        if (updateMyInfoRequest.getGender() != null && !updateMyInfoRequest.getGender().isEmpty())
            user.setGender(UserEntity.Gender.valueOf(updateMyInfoRequest.getGender()));

        if (updateMyInfoRequest.getDateOfBirth() != null && !updateMyInfoRequest.getDateOfBirth().isEmpty())
            user.setDateOfBirth(LocalDate.parse(updateMyInfoRequest.getDateOfBirth()));

        userJpa.save(user);

        return new ResponseDto(200, "회원정보 수정 완료", user.getNickName() + "님의 정보가 수정되었습니다.");
    }


    public ResponseDto updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("user 정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        String password = updatePasswordRequest.getPassword();
        String updatePassword = updatePasswordRequest.getUpdatePassword();
        String confirmPassword = updatePasswordRequest.getUpdatePasswordConfirm();

        if (!(passwordEncoder.matches(password, userEntity.getPassword())))
            throw new BadRequestException("기존 비밀번호가 일치하지 않습니다.", "");
        else if(!updatePassword.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$") ||!(updatePassword.length()>=8&&updatePassword.length()<=20)){
            throw new BadRequestException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.",password);
        } else if (!confirmPassword.equals(updatePassword)) {
            throw new BadRequestException("비밀번호와 비밀번호 확인이 같지 않습니다.","password : "+updatePassword+", password_confirm : "+confirmPassword);
        }else if (password.equals(updatePassword)) {
            throw new BadRequestException("기존 비밀번호와 변경 비밀번호가 일치합니다.", "기존: " + password + ", 변경: " + updatePassword);
        }

        userEntity.setPassword(passwordEncoder.encode(updatePassword));
        userJpa.save(userEntity);

        return new ResponseDto(200, "비밀번호 변경 성공", "비밀번호는 암호화 되어 저장되었습니다.");
    }


}
