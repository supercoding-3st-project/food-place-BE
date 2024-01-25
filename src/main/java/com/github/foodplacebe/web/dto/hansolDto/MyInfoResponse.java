package com.github.foodplacebe.web.dto.hansolDto;

import com.github.foodplacebe.repository.users.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyInfoResponse {
    private Integer userId;
    private String nickName;
    private String neighborhood;
    private UserEntity.Gender gender;
    private String profile_img;
    private LocalDateTime joinDate;
    private String status;
    private Integer registerPostCount;
    private Integer registerFavoriteCount;
    private Integer receiveFavoriteCount;
}
