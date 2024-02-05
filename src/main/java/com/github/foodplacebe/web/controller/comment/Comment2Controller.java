package com.github.foodplacebe.web.controller.comment;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.comment.Comment2Service;
import com.github.foodplacebe.web.dto.comment.CommentCreationDTO;
import com.github.foodplacebe.web.dto.comment.CommentModifyDTO;
import com.github.foodplacebe.web.dto.comment.CommentResponseDTO;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/comment")
@RequiredArgsConstructor
public class Comment2Controller {
    private final Comment2Service commentService;

    @GetMapping("/post")
    public ResponseDto getCommentByPostId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getCommentsByPostId(customUserDetails, postId, PageRequest.of(page,size));
    }

    @GetMapping("/comment")
    public ResponseDto getCommentByCommentId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Integer commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getCommentsByCommentId(customUserDetails, commentId, PageRequest.of(page,size));
    }

    @PostMapping("/add")
    public ResponseDto addComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentCreationDTO commentCreationDTO
        ) {
        return commentService.addComment(customUserDetails, commentCreationDTO);
    }

    @PutMapping("/mod")
    public ResponseDto modifyComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentModifyDTO commentModifyDTO
        ) {
        return commentService.modifyComment(customUserDetails, commentModifyDTO);
    }

    @DeleteMapping("/del/{commentId}")
    public ResponseDto deleteComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("commentId") Integer commentId ) {
        return commentService.deleteComment(customUserDetails, commentId);
    }

    @PostMapping("/like/{commentId}")
    public ResponseDto toggleCommentLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("commentId") Integer commentId ) {
        return commentService.toggleCommentLike(customUserDetails, commentId);
    }
}
