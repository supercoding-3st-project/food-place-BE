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
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseDto getFavoritePostsByUserId(CustomUserDetails customUserDetails, Pageable pageable) {
        try {
            UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                    .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
            Page<PostFavorite> postFavorites = postFavoriteJpa.findByUserEntity(userEntity, pageable);
            Page<FindPostsResponse> responses = postFavorites.map(postFavorite -> PostMapper.INSTANCE.postToFindPostsResponse(postFavorite.getPosts()));
            if (postFavorites.isEmpty()) {
//            throw new NotFoundException("즐겨찾기된 맛집이 없습니다.", customUserDetails.getUserId());
                return new ResponseDto(404, "즐겨찾기된 맛집이 없습니다.");
            }
            return new ResponseDto(200, "즐겨찾기된 맛집 조회 성공", responses);
        } catch (NotFoundException e) {
            return new ResponseDto(404, e.getMessage());
        } catch (Exception e) {
            return new ResponseDto(500, "서버 오류: " + e.getMessage());
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findRestaurantsByUserNeighborhood(CustomUserDetails customUserDetails, Pageable pageable) {
        try {
            UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                    .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
            String userEntityNeighborhood = userEntity.getNeighborhood();
            String[] userNeighborhoodParts = userEntityNeighborhood.split(" ");
            String userCity = userNeighborhoodParts[0];

            Page<Posts> restaurants = postsJpa.findRestaurantsByCity(userCity, pageable);
            Page<FindPostsResponse> responses = restaurants.map(PostMapper.INSTANCE::postToFindPostsResponse);
            if (restaurants.isEmpty()) {
                return new ResponseDto(404, "해당 도시에 맛집이 존재하지 않습니다.");
            }
            return new ResponseDto(200, "맛집 조회 성공", responses);
        } catch (NotFoundException e) {
            return new ResponseDto(404, e.getMessage());
        } catch (Exception e) {
            return new ResponseDto(500, "서버 오류: " + e.getMessage());
        }
    }

}
