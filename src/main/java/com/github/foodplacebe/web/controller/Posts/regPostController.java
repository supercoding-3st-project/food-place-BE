package com.github.foodplacebe.web.controller.Posts;

import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.PostsService.regPostService;
import com.github.foodplacebe.web.dto.postDto.PostRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class regPostController {
    private final regPostService regPostService;
    private final PostsJpa postsJpa;

    @Operation(summary = "맛집 게시물 작성")
    @PostMapping("/reg-post")
    public ResponseDto regPost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @RequestPart(value = "postRequest") PostRequest postRequest,
                               @RequestPart(value = "images") List<MultipartFile> multipartFiles) {

        return regPostService.regPost(customUserDetails, postRequest, multipartFiles);
    }

    @Operation(summary = "맛집 게시물 수정")
    @PutMapping("/modify-post/{postId}")
    public ResponseDto updatePost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestPart PostRequest postRequest,
                                  @PathVariable Integer postId){

        return regPostService.updatePost(customUserDetails, postRequest, postId);
    }
}
