package com.github.foodplacebe.repository.users;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.userRoles.UserRoles;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "userId")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "social_id", unique = true)
    private Long socialId;

    @Column(name = "nick_name", unique = true, nullable = false)
    private String nickName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

//    @Column(name = "id", unique = true, nullable = false)
//    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(255) default 'http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg'")
    private String imageUrl;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "status", columnDefinition = "varchar(255) default 'normal'")
    private String status;

    @Column(name = "failure_count", columnDefinition = "INT default 0")
    private Integer failureCount;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Column(name = "lock_date")
    private LocalDateTime lockDate;

    @OneToMany(mappedBy = "userEntity")
    private Collection<UserRoles> userRoles;

    @OneToMany(mappedBy = "userEntity")
    private Collection<Posts> posts;

    public enum Gender {
        남성, 여성, 불명
    }




//    @OneToMany(mappedBy = "userEntity")
//    private Collection<PostFavorite> postFavorites;
}
