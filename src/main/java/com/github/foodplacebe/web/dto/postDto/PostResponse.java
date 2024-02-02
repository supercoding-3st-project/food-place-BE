package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer postId;
    private Integer userId;

    private String name;

    private String address;
    private String detailAddress;
    private String neighborhood;

    private String latitude;
    private String longitude;

    private String contactNum;
    private String category;
    private String menu;
    private String content;

    private String mainPhoto;
    private List<String> images =new ArrayList<>();

    public PostResponse(Posts posts, List<PostPhotos> postPhotos){
        this.postId = posts.getPostId();
        this.userId = posts.getUserEntity().getUserId();
        this.name = posts.getName();
        this.address = posts.getAddress();
        this.detailAddress = posts.getDetailAddress();
        this.neighborhood = posts.getNeighborhood();
        this.latitude = posts.getLatitude();
        this.longitude = posts.getLongitude();
        this.contactNum = posts.getContactNum();
        this.category = posts.getCategory();
        this.menu = posts.getMenu();
        if(!postPhotos.isEmpty()){
            this.mainPhoto = postPhotos.get(0).getPhoto();
        }

        for (PostPhotos photos : postPhotos){
            this.images.add(photos.getPhoto());
        }
    }
}
