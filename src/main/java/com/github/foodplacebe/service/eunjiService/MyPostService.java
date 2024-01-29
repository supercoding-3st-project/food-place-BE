package com.github.foodplacebe.service.eunjiService;

import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.eunjiDto.SearchResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPostService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostFavoriteJpa postFavoriteJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto getMyPost(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        List<Posts> userPosts = postsJpa.findByUserEntity(userEntity);

        List<SearchResponse> searchResponses = new ArrayList<>();

        for (Posts post : userPosts) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setPostId(post.getPostId());
            searchResponse.setUserId(post.getUserEntity().getUserId());
            searchResponse.setName(post.getName());
            searchResponse.setMainPhoto(post.getMainPhoto());
            searchResponse.setViewCount(post.getViewCount());

            Integer favoriteCount = postFavoriteJpa.countByUserEntityAndPosts(userEntity, post);

            searchResponse.setFavoriteCount(favoriteCount);

            searchResponses.add(searchResponse);
        }

        return new ResponseDto(200,"내가 쓴 글 조회 완료", searchResponses);
    }

}

