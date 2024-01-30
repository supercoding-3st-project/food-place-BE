package com.github.foodplacebe.web.dto.posts;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer postId;
    private String name;
    private String neighborhood;
    private String category;
    private String menu;
    private Integer viewCount;
    private String mainPhoto;
    private LocalDateTime createAt;
    private Integer favoriteCount;
}
