package com.github.foodplacebe.web.dto.postDto;

import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String name;
    private String content;
    private String category;
    private String contactNum;
    private String address;
    private String detailAddress;
    private String latitude;
    private String longitude;
    private String mainPhoto;
    private List<String> postPhotos;

}
