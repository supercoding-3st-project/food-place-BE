package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String name;

    private String address;
    private String detailAddress;

    private String latitude;
    private String longitude;

    private String contactNum;
    private String category;

    private String menu;
    private String content;

    public void checkUpdate(PostRequest postRequest, Posts modifyPost) {
        if(Objects.isNull(postRequest.getName())){
            this.name = modifyPost.getName();
        }
        if(Objects.isNull(postRequest.getAddress())){
            this.name = modifyPost.getAddress();
        }
        if(Objects.isNull(postRequest.getDetailAddress())){
            this.name = modifyPost.getDetailAddress();
        }
        if(Objects.isNull(postRequest.getLatitude())){
            this.name = modifyPost.getLatitude();
        }
        if(Objects.isNull(postRequest.getLongitude())){
            this.name = modifyPost.getLongitude();
        }
        if(Objects.isNull(postRequest.getContactNum())){
            this.name = modifyPost.getContactNum();
        }
        if(Objects.isNull(postRequest.getMenu())){
            this.name = modifyPost.getMenu();
        }
        if(Objects.isNull(postRequest.getContent())){
            this.name = modifyPost.getContent();
        }
        if(Objects.isNull(postRequest.getCategory())){
            this.category = modifyPost.getCategory();
        }
    }
}
