package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.hansolService.PostService;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hansol")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post-register")  // 맛집 게시글 등록
    public ResponseDto postRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PostRegisterRequest postRegisterRequest) {
        return postService.postRegister(customUserDetails, postRegisterRequest);
    }

    @GetMapping("/posts")
    public ResponseDto findPosts(
            @RequestParam("area") String area,
            @RequestParam("category") String category,
            @RequestParam("order") String order,
            Pageable pageable
    ) {
        return postService.findPosts(area, category, order, pageable);
    }
}
