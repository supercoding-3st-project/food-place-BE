package com.github.foodplacebe.web.controller.authAccount.test;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.test.TestService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping("/lk")
    public ResponseDto lkDislk(HttpServletRequest httpServletRequest,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return testService.lkDislk(Integer.valueOf(httpServletRequest.getParameter("post-id")),
                customUserDetails);
    }

    @PostMapping(value = {"/{postId}/comments/{commentId}",
    "/{postId}/comments"})
    public ResponseDto createComment(@PathVariable Integer postId,
                                     @PathVariable(required = false) Integer commentId,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     @RequestBody TestDto testDto){
        return testService.createComment(postId, commentId, customUserDetails, testDto);
    }

    @GetMapping("/{postId}/comments")
    public ResponseDto getCommentsByPostId(@PathVariable Integer postId){
        return testService.getComments(postId);
    }

}
