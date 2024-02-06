package com.github.foodplacebe.web.controller.Posts;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.PostsService.PostsService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/post-detail/{postId}")
    public ResponseDto postDetails(@PathVariable Integer postId){
        return  postsService.postDetails(postId);
    }

    @GetMapping("/post-heart-status/{postId}")
    public ResponseDto likeHeartStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @PathVariable Integer postId ){
        return postsService.likeHeartStatus(customUserDetails,postId);
    }

    @PostMapping("/post-like-heart/{postId}")
    public ResponseDto likeHeart(@PathVariable Integer postId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return postsService.likeHeart(postId,customUserDetails);
    }

    @DeleteMapping("/delete-food/{postId}")
    public ResponseDto postsDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @PathVariable Integer postId){
        return postsService.postsDelete(postId,customUserDetails);
    }

    @GetMapping("/post-relation/{post-id}")
    public ResponseDto postRelated(
            @PathVariable("post-id") Integer postId,
            @RequestParam("order") String order
    ) {
        return postsService.findRelatedPosts(postId, order);

    }
}
