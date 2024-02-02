package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.postDto.PostRequest;
import com.github.foodplacebe.web.dto.postDto.PostResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class regPostService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostPhotosJpa postPhotosJpa;
    private final PostPhotosService postPhotosService;
    private final ConfigurationService configurationService;


    @Transactional(transactionManager = "tm")
    public ResponseDto regPost(CustomUserDetails customUserDetails, PostRequest postRequest, List<MultipartFile> multipartFiles) {
        // 유저 확인
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        configurationService.checkCategory(postRequest);
        String postNeighborhood = configurationService.determineNeighborhood(postRequest.getAddress());

        // 포스트 DB에 저장
        Posts newPost = Posts.builder()
                .userEntity(userEntity)
                .name(postRequest.getName())
                .address(postRequest.getAddress())
                .detailAddress(postRequest.getDetailAddress())
                .neighborhood(postNeighborhood)
                .latitude(postRequest.getLatitude())
                .longitude(postRequest.getLongitude())
                .contactNum(postRequest.getContactNum())
                .category(postRequest.getCategory())
                .menu(postRequest.getMenu())
                .content(postRequest.getContent())
                .viewCount(0)
                .build();
        postsJpa.save(newPost);
        // 이미지 DB에 저장
        if(multipartFiles!=null){
            uploadImage(newPost, multipartFiles);
        }

        postsJpa.save(newPost);

        PostResponse response = configurationService.response(newPost);

        return new ResponseDto(200, "게시물 등록 성공",  response);

    }


    public ResponseDto updatePost(CustomUserDetails customUserDetails, PostRequest postRequest, Integer postId) {
        // 유저 확인
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        // 내가 쓴 글이 맞는지 확인
        Posts modifyPost = checkMyPost(postId, userEntity);

        configurationService.checkCategory(postRequest);

        // DB에서 수정
        postRequest.checkUpdate(postRequest, modifyPost);
        Posts updatePost = Posts.builder()
                .postId(modifyPost.getPostId())
                .userEntity(modifyPost.getUserEntity())
                .name(postRequest.getName())
                .address(postRequest.getAddress())
                .detailAddress(postRequest.getDetailAddress())
                .build();

        return new ResponseDto(200,"포스트 수정 완료",  postId);
    }

    private Posts checkMyPost(Integer postId, UserEntity userEntity) {
        // 포스트 확인
        Posts myPost = postsJpa.findById(postId).orElseThrow(
                ()-> new NoSuchElementException("없는 포스트입니다.")
        );

        // 내가 쓴 포스트인지 확인
        if(!myPost.getUserEntity().equals(userEntity)){
            throw new IllegalArgumentException("다른 사람의 포스트입니다.");
        }

        return myPost;
    }

    public void uploadImage(Posts posts, List<MultipartFile> multipartFileList) {
        List<String> imageUrlList = postPhotosService.uploadImages(posts.getName(), multipartFileList);

        for (String imageUrl : imageUrlList) {
            PostPhotos postPhotos = PostPhotos.builder()
                    .posts(posts)
                    .photo(imageUrl)
                    .build();
            postPhotosJpa.save(postPhotos);
        }
    }
}
