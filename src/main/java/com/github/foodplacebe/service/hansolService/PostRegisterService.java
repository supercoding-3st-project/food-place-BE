package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.hansolService.hansolMappers.PostMapper;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostRegisterService {
    private final PostsJpa postsJpa;
    private final PostPhotosJpa postPhotosJpa;
    private final UserJpa userJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto postRegister(CustomUserDetails customUserDetails, PostRegisterRequest postRegisterRequest) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        Posts post = PostMapper.INSTANCE.postRegisterRequestToPosts(postRegisterRequest, userEntity);
        postsJpa.save(post);

        Integer postId = post.getPostId();

        if(postRegisterRequest.getPostPhotos().stream().count() > 4)
            throw new BadRequestException("맛집 사진은 4개까지만 등록 가능합니다.", "photo: " + postRegisterRequest.getPostPhotos().stream().count() + "개");

        postRegisterRequest.getPostPhotos().forEach(
                (ppr) -> {
                    PostPhotos postPhotos = PostPhotos.builder()
                            .posts(post)
                            .photo(ppr)
                            .build();
                    postPhotosJpa.save(postPhotos);
                }
        );

        return new ResponseDto(200, "게시물 등록 성공", "postId: " + postId);

    }
}
