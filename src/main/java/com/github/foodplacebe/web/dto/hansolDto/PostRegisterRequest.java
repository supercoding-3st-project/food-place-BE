package com.github.foodplacebe.web.dto.hansolDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRegisterRequest {
    private String name;
    private String address;
    private String detailAddress;
    private String latitude;
    private String longitude;
    private String category;
    private String contactNum;
    private String menu;
    private String content;
    private String mainPhoto;
    private List<String> postPhotos;
}
