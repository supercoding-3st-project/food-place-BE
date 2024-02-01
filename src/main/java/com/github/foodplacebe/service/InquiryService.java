package com.github.foodplacebe.service;


import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.hansolService.hansolMappers.PostMapper;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;
    private final UserJpa userJpa;


//    public Page<FindPostsResponse> getTopPostsByViewCount(int count, Pageable pageable) {
//        Page<FindPostsResponse> topByOrderByViewCountDesc = postsJpa.findTopByOrderByViewCountDesc(count, pageable);
//        return topByOrderByViewCountDesc;
//    }

    @Transactional(transactionManager = "tm")
    public List<FindPostsResponse> getFavoritePostsByUserId(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        List<PostFavorite> postFavorites = postFavoriteJpa.findByUserEntity(userEntity);
        if (postFavorites.isEmpty()) {
            throw new NotFoundException("즐겨찾기된 맛집이 없습니다.", customUserDetails.getUserId());
        }

        return postFavorites.stream()
                .map(postFavorite -> PostMapper.INSTANCE.postToFindPostsResponse(postFavorite.getPosts()))
                .collect(Collectors.toList());
    }

    @Transactional(transactionManager = "tm")
    public List<FindPostsResponse> findRestaurantsByUserNeighborhood(CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        String userEntityNeighborhood = userEntity.getNeighborhood();
        String[] userNeighborhoodParts = userEntityNeighborhood.split(" ");
        String userCity = userNeighborhoodParts[0];

        // 해당 도시의 맛집을 조회
        List<Posts> restaurants;
        try {
            restaurants = postsJpa.findRestaurantsByCity(userCity);
        } catch (Exception ex) {
            throw new BadRequestException("맛집 조회에 실패했습니다.", ex);
        }

        if (restaurants.isEmpty()) {
            throw new BadRequestException("해당 도시에 맛집이 존재하지 않습니다.", userCity);
        }

        // 맛집 정보를 FindPostsResponse로 매핑하여 반환
        return restaurants.stream()
                .map(PostMapper.INSTANCE::postToFindPostsResponse)
                .collect(Collectors.toList());
    }
}
