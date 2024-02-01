package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PostsDto {

    private Integer postId;
    private Integer userId;
    private String name;
    private String address;
    private String detailAddress;
    private String latitude;
    private String longitude;
    private String contactNum;
    private String category;
    private String menu;
    private String content;
    private Integer viewCount;
    private String mainPhoto;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer favoriteCount;
    private List<PostPhotoDto> postPhotoDtos;

}
