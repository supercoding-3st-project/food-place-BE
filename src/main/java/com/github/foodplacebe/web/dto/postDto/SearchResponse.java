package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.repository.posts.Posts;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResponse {
    private Integer postId;
    private Integer userId;
    private String name;
    private String mainPhoto;
    private Integer viewCount;
    private Integer favoriteCount;
    private String neighborhood;
    private String category;
    private String menu;
    private LocalDateTime createAt;
    private Boolean favoriteYn;

    public SearchResponse(Posts posts, int likeCount, Boolean favoriteYn) {
        this.postId = posts.getPostId();
        this.userId = posts.getUserEntity().getUserId();
        this.name = posts.getName();
        this.mainPhoto = posts.getMainPhoto();
        this.viewCount = posts.getViewCount();
        this.favoriteCount = likeCount;
        this.neighborhood = posts.getNeighborhood();
        this.category = posts.getCategory();
        this.menu = posts.getMenu();
        this.createAt = posts.getCreateAt();
        this.favoriteYn = favoriteYn;
    }
}
