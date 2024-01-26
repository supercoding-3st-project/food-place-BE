package com.github.foodplacebe.service.eunjiService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.mappers.PostMapper;
import com.github.foodplacebe.web.dto.eunjiDto.PostRequest;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class regPostService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostPhotosJpa postPhotosJpa;


    @Transactional(transactionManager = "tm")
    public ResponseDto getPost(Integer postId) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(()-> new NotFoundException("게시물을 찾을 수 없습니다.", postId));

        List<PostPhotos> postPhotos = post.getPostPhotos();
        List<PostPhotoDto> postPhotoDtos = postPhotos.stream().map(PostMapper.INSTANCE::postPhotosToPostPhotoDto).toList();

    }

    @Transactional(transactionManager = "tm")
    public ResponseDto regPost(CustomUserDetails customUserDetails, PostRequest postRequest) {
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));

        postsJpa.save(
                Posts.builder()
                        .name(postRequest.getName())
                        .content(postRequest.getContent())
                        .category(postRequest.getCategory())
                        .contactNum(postRequest.getContact_num())
                        .address(postRequest.getAddress())
                        .detailAddress(postRequest.getDetail_address())
                        .latitude(postRequest.getLatitude())
                        .longitude(postRequest.getLongitude())
                        .mainPhoto(postRequest.getMain_photo())
                        .postPhotos(postRequest.getPostPhotoDtos().stream().map(postRequest::))
        );
        return new ResponseDto(200,"포스트 등록 완료", "postId : " + postRequest);

    }

    @Transactional(transactionManager = "tm")
    public ResponseDto deletePost(CustomUserDetails customUserDetails, Integer postId) {
        Integer userId = customUserDetails.getUserId();
        UserEntity userEntity = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", userId));
        Posts existPost =postsJpa.findById(postId)
                .orElseThrow(()-> new NotFoundException("삭제할 포스트를 찾을 수 없습니다.", postId));

        if(!existPost.getUserEntity().equals(userEntity)){
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        postsJpa.delete(existPost);

        return new ResponseDto(200,"포스트 삭제 완료", "postId : " + postId);
    }


    public ResponseDto updatePost(CustomUserDetails customUserDetails, Integer postId) {

    }
}
