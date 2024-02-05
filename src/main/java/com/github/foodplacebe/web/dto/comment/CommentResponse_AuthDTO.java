package com.github.foodplacebe.web.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentResponse_AuthDTO {
    private Integer postId;
    private Integer userId;
    private Integer commentId;
    private Integer parentCommentId;
    private String content;
    private Integer likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean userLike;
}
