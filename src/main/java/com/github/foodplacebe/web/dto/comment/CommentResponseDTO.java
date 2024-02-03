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
public class CommentResponseDTO {
    private Integer postId;
    private Integer userId;
    private Integer commentId;
    private Integer parentCommentId;
    private String content;
    private Integer likeCount;
    private String profileImg;
    private String createDate;
    private String updateDate;

    public CommentResponseDTO(Integer postId, Integer userId, Integer commentId, Integer parentCommentId, String content, Integer likeCount, String profileImg, LocalDateTime createDate, LocalDateTime updateDate) {
        this.postId = postId;
        this.userId = userId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.likeCount = likeCount;
        this.profileImg = profileImg;
        this.createDate = createDate.toString().substring(0,10);
        this.updateDate = updateDate != null ? updateDate.toString().substring(0,10) : null;
    }
}



