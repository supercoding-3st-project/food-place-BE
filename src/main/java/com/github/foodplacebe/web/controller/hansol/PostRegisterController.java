package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.hansolService.PostRegisterService;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hansol")
@RequiredArgsConstructor
public class PostRegisterController {
    private final PostRegisterService postRegisterService;

    @PostMapping("/post-register")  // 맛집 게시글 등록
    public ResponseDto postRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PostRegisterRequest postRegisterRequest) {
        return postRegisterService.postRegister(customUserDetails, postRegisterRequest);
    }
}
