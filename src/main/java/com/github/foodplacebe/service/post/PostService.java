package com.github.foodplacebe.service.post;

import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.posts.PostRequest;
import com.github.foodplacebe.web.dto.posts.PostResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("postService")
@RequiredArgsConstructor
public class PostService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;

    public ResponseDto getPostList(CustomUserDetails customUserDetails, PostRequest postRequest, Pageable pageable) {

        UserEntity userEntity = null;
        if (customUserDetails != null) {
            userEntity = userJpa.findByEmailJoin(customUserDetails.getUsername())
                    .orElseThrow(()->new NotFoundException("계정 정보를 찾을 수 없습니다.",customUserDetails.getUsername()));
        }

        int userId;

        if (userEntity != null) {
            userId = userEntity.getUserId();
        } else {
            userId = 0;
        }

        Page<PostResponse> findPostList;
        findPostList = postsJpa.findPostsListAndFavorite(userId, postRequest.getNeighborhood(), postRequest.getCategory(), postRequest.getOrder(), pageable);
//        findPostList = postsJpa.findPostsList(postRequest.getNeighborhood(), postRequest.getCategory(), postRequest.getOrder(), pageable);

        List<PostResponse> updatedList = findPostList.getContent().stream()
                .map(postResponse -> {
                    if (userId != 0) {
                        Optional<Integer> userIdOptional = Optional.ofNullable(postResponse.getUserId());
                        postResponse.setFavoriteYn(userIdOptional.map(id -> "Y").orElse("N"));
                    } else {
                        postResponse.setFavoriteYn("N");
                    }
                    return postResponse;
                })
                .collect(Collectors.toList());

        findPostList = new PageImpl<>(updatedList, pageable, findPostList.getTotalElements());

        return new ResponseDto(200, "맛집 리스트 조회 성공", findPostList);
    }

}
