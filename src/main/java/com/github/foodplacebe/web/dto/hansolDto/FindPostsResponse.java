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
public class FindPostsResponse {
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
