package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.postDto.PostPhotosDto;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostFavoriteJpa postFavoriteJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto postDetails(Integer postId){
        Posts post = postsJpa.findById(postId)
                .orElseThrow(()-> new NotFoundException("게시물을 찾을 수 없습니다.",postId));

        post.setViewCount(post.getViewCount()+1);
        postsJpa.save(post);

        List<PostPhotos> postPhotos = post.getPostPhotos();
        Integer likeCount = Math.toIntExact(post.getPostFavorites().stream().count());

        List<PostPhotosDto> postPhotosDto = postPhotos.stream().map(PostsMapper.INSTANCE::postPhotosToPostPhotosDto).toList();
        PostDetailResponse postDetail = PostsMapper.INSTANCE.postsToPostDetailResponse(post,postPhotosDto,likeCount);

        return new ResponseDto(200,"맛집 조회 성공",postDetail);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto likeHeart(Integer postId,CustomUserDetails customUserDetails){
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(()->new NotFoundException("게시물을 찾을 수 없습니다.",postId));

        Integer userId = customUserDetails.getUserId();

        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.",userId));

        if(postFavoriteJpa.findByUserEntityAndPosts(userEntity,posts)==null){
            PostFavorite postFavorite = PostFavorite.builder()
                    .userEntity(userEntity)
                    .posts(posts)
                    .build();

            postFavoriteJpa.save(postFavorite);

            return new ResponseDto(200, "좋아요 등록", "postId : " + postId + ", userId : " + userId);
        }else {
            postFavoriteJpa.deleteByUserEntityAndPosts(userEntity, posts);
            return new ResponseDto(200, "좋아요 해제", "postId : " + postId + ", userId : " + userId);
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto likeHeartStatus(CustomUserDetails customUserDetails,Integer postId){
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(()-> new NotFoundException("게시물을 찾을 수 없습니다.", postId));

        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.",customUserDetails.getUserId()));

        Boolean heartStatus = postFavoriteJpa.findByUserEntityAndPosts(userEntity,posts) != null;

        return new ResponseDto(200,"좋아요 등록 확인",heartStatus);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto postsDelete(Integer postId, CustomUserDetails customUserDetails) {
        postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", postId));

        Integer userId = customUserDetails.getUserId();

        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        if(postsJpa.findByUserEntityAndPostId (userEntity,postId)==null){
            postsJpa.deleteById(postId);
        }

        return new ResponseDto(200, "맛집 삭제 성공", postId);
    }

    public ResponseDto findRelatedPosts(Integer postId, String order) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다.", postId));
        String address = post.getAddress();
        String name = post.getName();

        List<FindPostsResponse> findPostsResponse;

        if(order.equals("최신순")) {
            findPostsResponse = postsJpa.findFiveRelatedPosts(address, name)
                    .stream().sorted(Comparator.comparing(FindPostsResponse::getCreateAt).reversed()).limit(5)
                    .collect(Collectors.toList());
        } else if (order.equals("인기순")) {
            findPostsResponse = postsJpa.findFiveRelatedPosts(address, name)
                    .stream().sorted(Comparator.comparing(FindPostsResponse::getFavoriteCount).reversed()).limit(5)
                    .collect(Collectors.toList());
        }else if(order.equals("조회순")) {
            findPostsResponse = postsJpa.findFiveRelatedPosts(address, name)
                    .stream().sorted(Comparator.comparing(FindPostsResponse::getViewCount).reversed()).limit(5)
                    .collect(Collectors.toList());
        }else throw new BadRequestException("유효하지 않은 순서입니다.", order);

        return new ResponseDto(200, "관련 게시물 조회 완료", findPostsResponse);

    }
}
