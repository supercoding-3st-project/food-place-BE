package com.github.foodplacebe.web.dto.eunjiDto;

import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import lombok.*;

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

    public SearchResponse(Posts posts){
        this.postId = posts.getPostId();
        this.userId = posts.getUserEntity().getUserId();
        this.name = posts.getName();
        this.mainPhoto = posts.getMainPhoto();
        this.viewCount = posts.getViewCount();
    }
}
