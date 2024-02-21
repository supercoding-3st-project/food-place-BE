package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.repository.posts.Posts;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPostDto {
    private String name;

    private String address;
    private String detailAddress;

    private String latitude;
    private String longitude;

    private String contactNum;
    private String category;

    private String menu;
    private String content;

    private String mainPhoto;
    private List<String> images = new ArrayList<>();

    public void checkUpdate(Posts modifyPost, ModifyPostDto modifyPostDto) {
        if(Objects.isNull(modifyPostDto.getName())){
            this.name = modifyPost.getName();
        }
        if(Objects.isNull(modifyPostDto.getAddress())){
            this.address = modifyPost.getAddress();
        }
        if(Objects.isNull(modifyPostDto.getDetailAddress())){
            this.detailAddress = modifyPost.getDetailAddress();
        }
        if(Objects.isNull(modifyPostDto.getLatitude())){
            this.latitude = modifyPost.getLatitude();
        }
        if(Objects.isNull(modifyPostDto.getLongitude())){
            this.longitude = modifyPost.getLongitude();
        }
        if(Objects.isNull(modifyPostDto.getContactNum())){
            this.contactNum = modifyPost.getContactNum();
        }
        if(Objects.isNull(modifyPostDto.getMenu())){
            this.menu = modifyPost.getMenu();
        }
        if(Objects.isNull(modifyPostDto.getContent())){
            this.content = modifyPost.getContent();
        }
        if(Objects.isNull(modifyPostDto.getCategory())){
            this.category = modifyPost.getCategory();
        }

        this.images = modifyPostDto.getImages();
    }

}
