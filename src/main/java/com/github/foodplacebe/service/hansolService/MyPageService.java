package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.hansolDto.MyInfoResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserJpa userJpa;
    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;



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

    public ResponseDto findMyInfo(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        Integer registerPostCount = userEntity.getPosts() == null ? 0 : userEntity.getPosts().size();
        Integer registerFavoriteCount = userEntity.getPostFavorites() == null ? 0 : userEntity.getPostFavorites().size();

        List<Posts> postsByUser = postsJpa.findAllByUserEntity(userEntity);
        Integer receiveFavoriteCount = postsByUser.stream().map(post -> post.getPostFavorites().size()).mapToInt(Integer::intValue).sum();

        MyInfoResponse myInfoResponse = MyInfoResponse.builder()
                .userId(userEntity.getUserId())
                .nickName(userEntity.getNickName())
                .neighborhood(userEntity.getNeighborhood())
                .gender(userEntity.getGender())
                .profile_img(userEntity.getImageUrl())
                .joinDate(userEntity.getJoinDate())
                .status(userEntity.getStatus())
                .registerPostCount(registerPostCount)
                .registerFavoriteCount(registerFavoriteCount)
                .receiveFavoriteCount(receiveFavoriteCount)
                .build();

        return new ResponseDto(200, "내 정보 조회 성공", myInfoResponse);
    }
}
