package com.github.foodplacebe.web.dto.postDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPhotosDto {
    private Integer postPhotoId;
    private Integer postId;
    private String photo;
}
