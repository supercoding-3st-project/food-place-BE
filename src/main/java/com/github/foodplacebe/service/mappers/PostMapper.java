package com.github.foodplacebe.service.mappers;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target="postId", source = "posts.postId")

    PostPhotoDto postPhotosToPostPhotoDto(PostPhotos postPhotos);

    @Mapping(target="favoriteCount", source = "favoriteCount")
    @Mapping(target="userId", source = "posts.userEntity.userId")
    PostDetailResponse postsToPostDetailResponse(Posts posts, List<PostPhotoDto> postPhotoDtos, Integer favoriteCount);
}
