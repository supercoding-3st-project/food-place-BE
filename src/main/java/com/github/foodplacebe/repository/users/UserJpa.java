package com.github.foodplacebe.repository.users;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpa extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByNickName(String nickName);

    boolean existsByEmailAndEmailNot(String email, String loggedEmail);
    boolean existsByPhoneNumberAndEmailNot(String phoneNumber, String loggedEmail);
    boolean existsByNickNameAndEmailNot(String nickName, String loggedEmail);


    UserEntity findByEmail(String email);
    UserEntity findBySocialId(Long socialId);

    @Query(
            "SELECT ue " +
                    "FROM UserEntity ue " +
                    "JOIN FETCH ue.userRoles uer " +
                    "JOIN FETCH uer.roles " +
                    "WHERE ue.socialId = :socialId"
    )
    Optional<UserEntity> findBySocialIdJoin(Long socialId);

    @Query(
            "SELECT ue " +
                    "FROM UserEntity ue " +
                    "JOIN FETCH ue.userRoles uer " +
                    "JOIN FETCH uer.roles " +
                    "WHERE ue.phoneNumber = ?1"
    )
    Optional<UserEntity> findByPhoneNumberJoin(String phoneNumber);


    @Query(
            "SELECT ue " +
            "FROM UserEntity ue " +
            "JOIN FETCH ue.userRoles uer " +
            "JOIN FETCH uer.roles " +
            "WHERE ue.email = :email"
    )
    Optional<UserEntity> findByEmailJoin(String email);

    @Query(
            "SELECT ue " +
                    "FROM UserEntity ue " +
                    "JOIN FETCH ue.userRoles uer " +
                    "JOIN FETCH uer.roles " +
                    "WHERE ue.nickName = :nickName"
    )
    Optional<UserEntity> findByNickNameJoin(String nickName);

}
