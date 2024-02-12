package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.postDto.ModifyPostDto;
import com.github.foodplacebe.web.dto.postDto.PostRequest;
import com.github.foodplacebe.web.dto.postDto.PostResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class regPostService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostPhotosJpa postPhotosJpa;
    private final PostPhotosService postPhotosService;
    private final ConfigurationService configurationService;

    @Transactional(transactionManager = "tm")
    public ResponseDto regPost(CustomUserDetails customUserDetails, PostRequest postRequest,
                               List<MultipartFile> multipartFiles) {
        // 유저 확인
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        configurationService.checkCategory(postRequest.getCategory());
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
                .createAt(LocalDateTime.now())
                .viewCount(0)
                .build();
        postsJpa.save(newPost);
        // 이미지 DB에 저장
        if (multipartFiles != null) {
            uploadImage(newPost, multipartFiles);
        }

        PostResponse response = configurationService.response(newPost);

        return new ResponseDto(200, "게시물 등록 성공", response);

    }


    public ResponseDto updatePost(CustomUserDetails customUserDetails, ModifyPostDto modifyPostDto,
                                  List<MultipartFile> multipartFiles, Integer postId) {
        // 유저 확인
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        // 내가 쓴 글이 맞는지 확인
        Posts modifyPost = checkMyPost(postId, userEntity);

        // DB에서 수정
        modifyPostDto.checkUpdate(modifyPost, modifyPostDto);

        configurationService.checkCategory(modifyPostDto.getCategory());
        String postNeighborhood = configurationService.determineNeighborhood(modifyPostDto.getAddress());

        modifyPost.setName(modifyPostDto.getName());
        modifyPost.setAddress(modifyPostDto.getAddress());
        modifyPost.setDetailAddress(modifyPostDto.getDetailAddress());
        modifyPost.setLatitude(modifyPostDto.getLatitude());
        modifyPost.setLongitude(modifyPostDto.getLongitude());
        modifyPost.setContactNum(modifyPostDto.getContactNum());
        modifyPost.setMenu(modifyPostDto.getMenu());
        modifyPost.setContent(modifyPostDto.getContent());
        modifyPost.setCategory(modifyPostDto.getCategory());
        modifyPost.setNeighborhood(postNeighborhood);

        postsJpa.save(modifyPost);

        // s3에 저장된 이미지 리스트 불러오기
        List<String> s3ImageUrlList = postPhotosJpa.findByPostsPostId(postId);

        // 이미지 추가 및 삭제 비교 및 처리
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            MultipartFile file = multipartFiles.get(0); // 단일 이미지 파일에 대해서만 처리
            String newImageUrl = uploadImage(modifyPost, file);
            // 새 이미지가 아직 추가되지 않은 경우에만 추가
            if (!s3ImageUrlList.contains(newImageUrl)) {
                s3ImageUrlList.add(newImageUrl);
            }
        }

        // s3ImageUrlList와 multipartFiles의 차이를 통해 이미지 삭제 여부 결정
        List<String> imagesToDelete = new ArrayList<>(s3ImageUrlList);
        imagesToDelete.removeAll(multipartFiles.stream()
                .map(file -> uploadImage(modifyPost, file))
                .collect(Collectors.toList()));

        for (String imageUrl : imagesToDelete) {
            postPhotosService.deleteImage(imageUrl);
            postPhotosJpa.deleteByPhoto(imageUrl);
        }

        postsJpa.save(modifyPost);

        PostResponse response = configurationService.response(modifyPost);

        postsJpa.save(modifyPost);

        return new ResponseDto(200, "포스트 수정 완료", response);
    }

    private String uploadImage(Posts modifyPost, MultipartFile file) {
        String imageUrl = postPhotosService.uploadImageV1(modifyPost.getPostId(), file);

        PostPhotos photos = PostPhotos.builder()
                .posts(modifyPost)
                .photo(imageUrl)
                .build();
        postPhotosJpa.save(photos);

        return imageUrl;
    }

    private Posts checkMyPost(Integer postId, UserEntity userEntity) {
        // 포스트 확인
        Posts myPost = postsJpa.findById(postId).orElseThrow(
                () -> new NoSuchElementException("없는 포스트입니다.")
        );

        // 내가 쓴 포스트인지 확인
        if (!myPost.getUserEntity().equals(userEntity)) {
            throw new IllegalArgumentException("다른 사람의 포스트입니다.");
        }

        return myPost;
    }



    public void uploadImage(Posts posts, List<MultipartFile> multipartFileList) {
        List<String> imageUrlList = postPhotosService.uploadImage(posts.getPostId(), multipartFileList);

        for (String imageUrl : imageUrlList) {
            PostPhotos postPhotos = PostPhotos.builder()
                    .posts(posts)
                    .photo(imageUrl)
                    .build();
            postPhotosJpa.save(postPhotos);
        }
    }
}
