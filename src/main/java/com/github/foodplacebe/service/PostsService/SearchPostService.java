package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.postDto.SearchResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchPostService {
    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;
    private final UserJpa userJpa;


    @Transactional(transactionManager = "tm")
    public ResponseDto findAddress(String address, Pageable pageable, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Page<Posts> postsList = postsJpa.findByAddressContaining(address, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + address);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                Boolean favoriteYn = postFavoriteJpa.existsByUserEntityAndPosts(userEntity, post);
                searchResponses.add(new SearchResponse(post, likeCount, favoriteYn));
            }

            return new ResponseDto(200, "주소로 검색 완료", searchResponses);
        }
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto findMenu(String menu, Pageable pageable, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Page<Posts> postsList = postsJpa.findByMenuContaining(menu, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + menu);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                Boolean favoriteYn = postFavoriteJpa.existsByUserEntityAndPosts(userEntity, post);
                searchResponses.add(new SearchResponse(post, likeCount, favoriteYn));
            }

            return new ResponseDto(200, "메뉴로 검색 완료", searchResponses);
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findName(String name, Pageable pageable, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Page<Posts> postsList = postsJpa.findByNameContaining(name, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + name);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                Boolean favoriteYn = postFavoriteJpa.existsByUserEntityAndPosts(userEntity, post);
                searchResponses.add(new SearchResponse(post, likeCount, favoriteYn));
            }

            return new ResponseDto(200, "이름으로 검색 완료", searchResponses);
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findKeyword(String keyword, Pageable pageable, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));


        Page<Posts> postsList = postsJpa.findByAddressOrMenuOrNameContaining(keyword, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + keyword);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                Boolean favoriteYn = postFavoriteJpa.existsByUserEntityAndPosts(userEntity, post);
                searchResponses.add(new SearchResponse(post, likeCount, favoriteYn));
            }

            return new ResponseDto(200, "키워드로 검색 완료", new PageImpl<>(searchResponses,pageable, searchResponses.size()));
        }
    }
}