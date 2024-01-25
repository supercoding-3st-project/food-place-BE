package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserJpa userJpa;
    private final PostsJpa postsJpa;


    public ResponseDto findMyPosts(CustomUserDetails customUserDetails, Pageable pageable) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Page<FindPostsResponse> posts = postsJpa.findAllByPostsByPagination(userEntity.getUserId(), pageable);
        return new ResponseDto(200, "나의 맛집 게시물 조회 성공", posts);

    }

    public ResponseDto findMyFavorite(CustomUserDetails customUserDetails, Pageable pageable) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Page<FindPostsResponse> posts = postsJpa.findAllByFavoriteByPagination(userEntity.getUserId(), pageable);
        return new ResponseDto(200, "내가 찜한 게시물 조회 성공", posts);
    }
}
