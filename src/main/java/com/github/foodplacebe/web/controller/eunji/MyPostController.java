package com.github.foodplacebe.web.controller.eunji;


import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.eunjiService.MyPostService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class MyPostController {
    private final MyPostService mypostService;

    @GetMapping("/mypost")
    @Operation(summary = "내가 쓴 게시글 조회")
    public ResponseDto getMyPost(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return mypostService.getMyPost(customUserDetails);
    }
}
