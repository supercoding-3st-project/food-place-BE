package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.exceptions.AccessDenied;
import com.github.foodplacebe.service.hansolService.CommentService;
import com.github.foodplacebe.web.dto.hansolDto.CommentRegisterRequest;
import com.github.foodplacebe.web.dto.hansolDto.CommentUpdateRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hansol")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment-register") // 댓글 작성
    public ResponseDto commentRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommentRegisterRequest commentRegisterRequest) {
        return commentService.commentRegister(customUserDetails, commentRegisterRequest);
    }


    @GetMapping("/comments/{postId}") // 부모 댓글 조회(pagination)
    public ResponseDto comments (@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("postId") Integer postId, Pageable pageable) {
        if (customUserDetails==null) throw new AccessDenied("로그인을 해주세요.", "");
        return commentService.findComments(postId, pageable);
    }

    @GetMapping("/comments-son/{postId}") // 자식 댓글 조회(pagination) => 대댓글은 depth=1까지 즉, 댓글(부모)의 대댓글(자식)까지만 존재한다.(대댓글(자식)의 대댓글(자식) X)
    public ResponseDto commentsSon(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("postId") Integer postId, @RequestParam("parents-comment-id") Integer parentsCommentId, Pageable pageable) {
        if (customUserDetails==null) throw new AccessDenied("로그인을 해주세요.", "");
        return commentService.findCommentsSon(postId, parentsCommentId, pageable);
    }

    @PutMapping("/comment-update") // 댓글 수정
    public ResponseDto commentUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return commentService.commentUpdate(customUserDetails, commentUpdateRequest);
    }

    @DeleteMapping("/comment-delete/{commentId}") // 댓글 삭제
    public ResponseDto commentDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("commentId") Integer commentId) {
        return commentService.commentDelete(customUserDetails, commentId);
    }

    @PostMapping("/comment-favorite/{commentId}") // 댓글 좋아요 등록
    public ResponseDto commentFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("commentId") Integer commentId) {
        return commentService.commentFavorite(customUserDetails, commentId);
    }

}
