package com.github.foodplacebe.web.dto.eunjiDto;

import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String name;
    private String content;
    private String category;
    private String contact_num;
    private String address;
    private String detail_address;
    private String latitude;
    private String longitude;
    private String main_photo;
    private List<PostPhotoDto> postPhotoDtos;

}
