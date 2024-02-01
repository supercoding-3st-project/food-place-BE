package com.github.foodplacebe.service.hansolService.hansolMappers;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
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

    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(target = "userEntity", source = "userEntity")
    @Mapping(target = "postPhotos", ignore = true)
    @Mapping(target = "neighborhood", source = "neighborhood")
    Posts postRegisterRequestToPosts(PostRegisterRequest postRegisterRequest, UserEntity userEntity, String neighborhood);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "neighborhood", source = "neighborhood")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "menu", source = "menu")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "mainPhoto", source = "mainPhoto")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "favoriteCount", expression = "java(post.getPostFavorites().size())")
    FindPostsResponse postToFindPostsResponse(Posts post);


}
