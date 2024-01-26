package com.github.foodplacebe.web.dto.hansolDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRegisterRequest {
    private Integer postId;
    private Integer parentCommentId;
    private String content;
}
