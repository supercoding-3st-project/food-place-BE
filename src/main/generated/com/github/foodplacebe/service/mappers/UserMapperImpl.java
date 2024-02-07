package com.github.foodplacebe.service.mappers;

import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.account.AccountDto;
import com.github.foodplacebe.web.dto.account.AccountPatchDto;
import com.github.foodplacebe.web.dto.account.SignUpRequest;
import com.github.foodplacebe.web.dto.account.SignUpResponse;
import com.github.foodplacebe.web.dto.account.SocialAccountDto;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-28T23:21:56+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity signUpRequestToUserEntity(SignUpRequest signUpRequest) {
        if ( signUpRequest == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setNickName( signUpRequest.getNickName() );
        userEntity.setEmail( signUpRequest.getEmail() );
        userEntity.setPassword( signUpRequest.getPassword() );
        if ( signUpRequest.getGender() != null ) {
            userEntity.setGender( Enum.valueOf( UserEntity.Gender.class, signUpRequest.getGender() ) );
        }

        userEntity.setJoinDate( java.time.LocalDateTime.now() );
        userEntity.setDateOfBirth( birthFormatting(signUpRequest.getDateOfBirth()) );
        userEntity.setFailureCount( 0 );
        userEntity.setStatus( "normal" );
        userEntity.setImageUrl( defaultProfileImgSetting() );

        return userEntity;
    }

    @Override
    public SignUpResponse userEntityToSignUpResponse(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        SignUpResponse signUpResponse = new SignUpResponse();

        signUpResponse.setNickName( userEntity.getNickName() );

        signUpResponse.setJoinDate( formatting(userEntity.getJoinDate()) );

        return signUpResponse;
    }

    @Override
    public AccountDto userEntityToAccountDTO(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        AccountDto accountDto = new AccountDto();

        accountDto.setEmail( userEntity.getEmail() );
        accountDto.setNickName( userEntity.getNickName() );
        accountDto.setPhoneNumber( userEntity.getPhoneNumber() );
        accountDto.setImageUrl( userEntity.getImageUrl() );
        accountDto.setNeighborhood( userEntity.getNeighborhood() );
        if ( userEntity.getGender() != null ) {
            accountDto.setGender( userEntity.getGender().name() );
        }
        if ( userEntity.getDateOfBirth() != null ) {
            accountDto.setDateOfBirth( DateTimeFormatter.ISO_LOCAL_DATE.format( userEntity.getDateOfBirth() ) );
        }
        accountDto.setJoinDate( formatting( userEntity.getJoinDate() ) );

        return accountDto;
    }

    @Override
    public UserEntity AccountDTOToUserEntity(AccountDto accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setNickName( accountDTO.getNickName() );
        userEntity.setPhoneNumber( accountDTO.getPhoneNumber() );
        userEntity.setEmail( accountDTO.getEmail() );
        userEntity.setNeighborhood( accountDTO.getNeighborhood() );
        if ( accountDTO.getGender() != null ) {
            userEntity.setGender( Enum.valueOf( UserEntity.Gender.class, accountDTO.getGender() ) );
        }
        userEntity.setImageUrl( accountDTO.getImageUrl() );
        userEntity.setDateOfBirth( birthFormatting( accountDTO.getDateOfBirth() ) );

        return userEntity;
    }

    @Override
    public UserEntity socialAccountDtoToUserEntity(SocialAccountDto socialAccountDto) {
        if ( socialAccountDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setSocialId( socialAccountDto.getSocialId() );
        userEntity.setNickName( socialAccountDto.getNickName() );
        userEntity.setEmail( socialAccountDto.getEmail() );
        if ( socialAccountDto.getGender() != null ) {
            userEntity.setGender( Enum.valueOf( UserEntity.Gender.class, socialAccountDto.getGender() ) );
        }
        userEntity.setImageUrl( socialAccountDto.getImageUrl() );
        userEntity.setDateOfBirth( birthFormatting( socialAccountDto.getDateOfBirth() ) );

        return userEntity;
    }

    @Override
    public UserEntity AccountPatchDtoToUserEntity(AccountPatchDto accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setNickName( accountDTO.getNickName() );
        userEntity.setPhoneNumber( accountDTO.getPhoneNumber() );
        userEntity.setEmail( accountDTO.getEmail() );
        userEntity.setPassword( accountDTO.getPassword() );
        userEntity.setNeighborhood( accountDTO.getNeighborhood() );
        if ( accountDTO.getGender() != null ) {
            userEntity.setGender( Enum.valueOf( UserEntity.Gender.class, accountDTO.getGender() ) );
        }
        userEntity.setImageUrl( accountDTO.getImageUrl() );

        return userEntity;
    }
}
