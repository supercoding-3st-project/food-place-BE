package com.github.foodplacebe.web.controller.eunji;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.eunjiService.regPostService;
import com.github.foodplacebe.web.dto.eunjiDto.PostRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class regPostController {
    private final regPostService regPostService;

    @GetMapping("/get-post/{postId}")
    public ResponseDto getPost(@PathVariable Integer postId){
        return regPostService.getPost(postId);
    }

    @PostMapping("/get-post/{postId}")
    public ResponseDto updatePost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @PathVariable Integer postId){
        return regPostService.updatePost(customUserDetails, postId);
    }

    @PostMapping("/reg-post")
    public ResponseDto regPost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @RequestBody PostRequest postRequest)
    {
        return regPostService.regPost(customUserDetails,postRequest);
    }

    @DeleteMapping("/get-post/{postId}")
    public ResponseDto deletePost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @PathVariable Integer postId){
        return regPostService.deletePost(customUserDetails, postId);
    }


}
