package com.github.foodplacebe.web.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentModifyDTO {
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private Integer parentCommentId;
    private String content;
}
