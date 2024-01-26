package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.commentFavorite.CommentFavoriteJpa;
import com.github.foodplacebe.repository.comments.CommentsJpa;
import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.hansolService.hansolMappers.PostMapper;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostDetailService {

    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;
    private final UserJpa userJpa;
    private final PostPhotosJpa postPhotosJpa;
    private final CommentFavoriteJpa commentFavoriteJpa;
    private final CommentsJpa commentsJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto getPostDetails(Integer postId) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(()-> new NotFoundException("게시물을 찾을 수 없습니다.", postId));

        post.setViewCount(post.getViewCount() + 1); // 조회수 +1
        postsJpa.save(post);

        List<PostPhotos> postPhotos = post.getPostPhotos();
        Integer favoriteCount = (int) post.getPostFavorites().stream().count();

        List<PostPhotoDto> postPhotoDtos = postPhotos.stream().map(PostMapper.INSTANCE::postPhotosToPostPhotoDto).toList();
        PostDetailResponse postDetailResponse1 = PostMapper.INSTANCE.postsToPostDetailResponse(post, postPhotoDtos, favoriteCount);

        return new ResponseDto(200, "상품 디테일 조회 성공", postDetailResponse1);
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto pushFavorite(Integer postId, CustomUserDetails customUserDetails) {
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", postId));
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));


        if (postFavoriteJpa.findByUserEntityAndPosts(userEntity, posts)==null) {
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

    public ResponseDto getFavoriteStatus (CustomUserDetails customUserDetails, Integer postId) {
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", postId));
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        Boolean favoriteStatus = postFavoriteJpa.findByUserEntityAndPosts(userEntity, posts) != null;

        return new ResponseDto(200, "좋아요 등록 여부 확인", favoriteStatus);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto deletePost(Integer postId, CustomUserDetails customUserDetails) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", postId));

        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        if (Objects.equals(post.getUserEntity().getUserId(), userEntity.getUserId())) {
            postFavoriteJpa.deleteAllByPosts(post); // post_favorite 데이터 삭제
            postPhotosJpa.deleteAllByPosts(post);  // post_photos 데이터 삭제
            post.getComments().forEach(commentFavoriteJpa::deleteAllByComments);  // comment_favorite 데이터 삭제
            commentsJpa.deleteAllByPosts(post);  // comments 데이터 삭제
            postsJpa.deleteById(post.getPostId());  // posts 데이터 삭제
        }
        return new ResponseDto(200, "게시물 좋아요, 게시물 사진, 댓글, 댓글 좋아요, 게시글 삭제 완료", postId);
    }
}
