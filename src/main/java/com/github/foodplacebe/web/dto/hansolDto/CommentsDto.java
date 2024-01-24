package com.github.foodplacebe.web.dto.hansolDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String content;
    private Integer parentCommentId;
    private Boolean deleteStatus;
    private LocalDateTime createAt;
    private LocalDateTime deleteAt;
    private LocalDateTime updateAt;
}
