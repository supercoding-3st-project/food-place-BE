package com.github.foodplacebe.web.controller.post;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.post.PostService;
import com.github.foodplacebe.web.dto.posts.PostRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("postController")
@RequestMapping("/v1/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post-list")
    public ResponseDto getPostList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @RequestBody PostRequest postRequest,
                                   Pageable pageable){
        return postService.getPostList(customUserDetails, postRequest, pageable);
    }
}
