package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.commentFavorite.CommentFavorite;
import com.github.foodplacebe.repository.commentFavorite.CommentFavoriteJpa;
import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.comments.CommentsJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.AccessDenied;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.hansolDto.CommentRegisterRequest;
import com.github.foodplacebe.web.dto.hansolDto.CommentResponse;
import com.github.foodplacebe.web.dto.hansolDto.CommentUpdateRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final UserJpa userJpa;
    private final CommentsJpa commentsJpa;
    private final CommentFavoriteJpa commentFavoriteJpa;
    private final PostsJpa postsJpa;

    public ResponseDto commentRegister(CustomUserDetails customUserDetails, CommentRegisterRequest commentRegisterRequest) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Posts post = postsJpa.findById(commentRegisterRequest.getPostId())
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", commentRegisterRequest.getPostId()));

        Comments comments = Comments.builder()
                .posts(post)
                .userEntity(userEntity)
                .content(commentRegisterRequest.getContent())
                .parentCommentId(commentRegisterRequest.getParentCommentId()==null? 0 : commentRegisterRequest.getParentCommentId())
                .deleteStatus(false)
                .createAt(LocalDateTime.now())
                .build();
        commentsJpa.save(comments);

        return new ResponseDto(200, "댓글 등록 성공", "commentId: " + comments.getCommentId());
    }


    public ResponseDto findComments(Integer postId, Pageable pageable) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.",postId));

        Page<CommentResponse> comments = commentsJpa.findAllComments(postId, pageable);
        if (comments.isEmpty()) throw new NotFoundException("부모 댓글이 없습니다.", "postId: " + postId);
        return new ResponseDto(200, "부모 댓글 조회 성공", comments);
    }


    public ResponseDto findCommentsSon(Integer postId, Integer parentsCommentId, Pageable pageable) {
        Posts post = postsJpa.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.",postId));

        Page<CommentResponse> comments = commentsJpa.findAllCommentsByCommentId(postId, parentsCommentId, pageable);
        if (comments.isEmpty()) throw new NotFoundException("자식 댓글이 없습니다.", "부모 댓글 ID: " + parentsCommentId);
        return new ResponseDto(200, "자식 댓글 조회 성공", comments);
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto commentUpdate(CustomUserDetails customUserDetails, CommentUpdateRequest commentUpdateRequest) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Comments comment = commentsJpa.findById(commentUpdateRequest.getCommentId())
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다.", commentUpdateRequest.getCommentId()));

        if (comment.getDeleteStatus()) throw new BadRequestException("이미 삭제된 댓글입니다.", comment.getCommentId());
        if (!userEntity.getUserId().equals(comment.getUserEntity().getUserId()))
            throw new AccessDenied("유저정보가 일치하지 않습니다.", "로그인 유저: " + userEntity.getUserId() + "!= 댓글 작성자: " + comment.getUserEntity().getUserId());

        comment.setContent(commentUpdateRequest.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 수정 완료", comment.getContent());
    }


    public ResponseDto commentDelete(CustomUserDetails customUserDetails, Integer commentId) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Comments comment = commentsJpa.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다.", commentId));

        if (!userEntity.getUserId().equals(comment.getUserEntity().getUserId()))
            throw new AccessDenied("유저정보가 일치하지 않습니다.", "로그인 유저: " + userEntity.getUserId() + "!= 댓글 작성자: " + commentId);
        if (comment.getDeleteStatus()) throw new BadRequestException("이미 삭제된 댓글입니다.", comment.getCommentId());

        comment.setDeleteStatus(true);
        comment.setDeleteAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 삭제 완료", comment.getCommentId());
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto commentFavorite(CustomUserDetails customUserDetails, Integer commentId) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));
        Comments comment = commentsJpa.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다.", commentId));

        if (comment.getDeleteStatus()) throw new BadRequestException("삭제된 댓글입니다.", commentId); // 삭제된 댓글 인지 확인

        if (commentFavoriteJpa.findByUserEntityAndComments(userEntity, comment) == null) { // 좋아요 누르지 않았을 때는 등록
            CommentFavorite newCommentFavorite = CommentFavorite.builder()
                    .userEntity(userEntity)
                    .comments(comment)
                    .build();
            commentFavoriteJpa.save(newCommentFavorite);
            return new ResponseDto(200, "좋아요 등록 성공", newCommentFavorite.getCommentFavoriteId());
        }else {
            commentFavoriteJpa.deleteByUserEntityAndComments(userEntity, comment); // 좋아요를 이미 눌렀을때는 해제
            return new ResponseDto(200, "좋아요 해제 성공", "userId: " + userEntity.getUserId() + " commentId: " + commentId);
        }
    }
}
