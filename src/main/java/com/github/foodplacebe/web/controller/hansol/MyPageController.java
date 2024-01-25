package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.hansolService.MyPageService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hansol")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/find-my-posts")
    public ResponseDto findMyPosts(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        return myPageService.findMyPosts(customUserDetails, pageable);
    }

    @GetMapping("/find-my-favorite")
    public ResponseDto findMyFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        return myPageService.findMyFavorite(customUserDetails, pageable);
    }

    @GetMapping("/find-my-info")
    public ResponseDto findMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.findMyInfo(customUserDetails);
    }
}
