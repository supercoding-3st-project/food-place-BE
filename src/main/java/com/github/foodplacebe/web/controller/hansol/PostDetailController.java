package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.hansolService.PostDetailService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class PostDetailController {
    private final PostDetailService postDetailService;

    @GetMapping("/get-post-detail/{postId}")
    public ResponseDto getPostDetails(@PathVariable Integer postId) {
        return postDetailService.getPostDetails(postId);
    }

    @PostMapping("/post-favorite/{postId}")
    public ResponseDto pushFavorite(
            @PathVariable Integer postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return postDetailService.pushFavorite(postId,customUserDetails);
    }
}
