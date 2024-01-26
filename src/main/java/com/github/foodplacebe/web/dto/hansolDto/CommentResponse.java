package com.github.foodplacebe.web.dto.hansolDto;

import com.github.foodplacebe.repository.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String content;
    private Integer parentCommentId;
    private Boolean deleteStatus;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private Integer favoriteCount;
    private String nickName;
    private String neighborhood;
    private UserEntity.Gender gender;
    private String profileImage;
}
