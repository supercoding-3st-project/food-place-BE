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

    @GetMapping("/get-post-detail/{postId}") // 상품 상세 조회
    public ResponseDto getPostDetails(@PathVariable Integer postId) {
        return postDetailService.getPostDetails(postId);
    }

    @GetMapping("/post-favorite-status/{postId}") // 로그인한 유저가 좋아요를 눌렀는지 확인
    public ResponseDto getFavoriteStatus (@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer postId) {
        return postDetailService.getFavoriteStatus(customUserDetails, postId);
    }
    @PostMapping("/post-favorite-push/{postId}") // 좋아요 누르기(등록/해제)
    public ResponseDto pushFavorite(@PathVariable Integer postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return postDetailService.pushFavorite(postId,customUserDetails);
    }


}
