package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.postDto.PostPhotosDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostsMapper {
    PostsMapper INSTANCE = Mappers.getMapper(PostsMapper.class);

    @Mapping(target="postId", source = "posts.postId")
    PostPhotosDto postPhotosToPostPhotosDto(PostPhotos postPhotos);

    @Mapping(target="favoriteCount", source = "favoriteCount")
    @Mapping(target="userId", source = "posts.userEntity.userId")
    PostDetailResponse postsToPostDetailResponse(Posts posts, List<PostPhotosDto> postPhotoDtos, Integer favoriteCount);
}
