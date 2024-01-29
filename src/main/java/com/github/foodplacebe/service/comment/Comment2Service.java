package com.github.foodplacebe.service.comment;

import com.github.foodplacebe.repository.commentFavorite.CommentFavorite;
import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.comment.CommentCreationDTO;
import com.github.foodplacebe.web.dto.comment.CommentModifyDTO;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import com.github.foodplacebe.repository.commentFavorite.CommentFavoriteJpa;
import com.github.foodplacebe.repository.comments.CommentsJpa;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.foodplacebe.web.dto.comment.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class Comment2Service {
    private final CommentsJpa commentsJpa;
    private final CommentFavoriteJpa commentFavoriteJpa;
    private final UserJpa userJpa;
    private final PostsJpa postsJpa;

    public Page<CommentResponseDTO> getCommentsByPostId(Integer postId, Pageable pageable){
        return commentsJpa.findByPostsPostIdAndParentCommentId(postId, 0, pageable)
                .map(this::convertToCommentResponseDTO);
    }


    public Page<CommentResponseDTO> getCommentsByCommentId(Integer commentId, Pageable pageable){
        return commentsJpa.findByParentCommentId(commentId, pageable)
                .map(this::convertToCommentResponseDTO);
    }

    private CommentResponseDTO convertToCommentResponseDTO(Comments comment){
        int likeCount = commentFavoriteJpa.countByCommentsCommentId(comment.getCommentId());
        return new CommentResponseDTO(
                comment.getPosts().getPostId(),
                comment.getUserEntity().getUserId(),
                comment.getCommentId(),
                comment.getParentCommentId(),
                comment.getContent(),
                likeCount,
                comment.getCreateAt(),
                comment.getUpdateAt()
        );
    }

    public ResponseDto addComment(CustomUserDetails customUserDetails, CommentCreationDTO commentCreationDTO) {
        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("User not found", customUserDetails.getUserId()));

        Posts post = postsJpa.findById(commentCreationDTO.getPostId())
                .orElseThrow(()->new NotFoundException("Post not found", commentCreationDTO.getPostId()));

        if( authUser.getUserId().equals( commentCreationDTO.getUserId() ) == false ) {
            throw new NotFoundException("Auth Error", commentCreationDTO.getUserId() );
        }

        if( commentCreationDTO.getParentCommentId() != null ) {
            boolean isChk = commentsJpa.existsByCommentIdAndDeleteStatus(commentCreationDTO.getParentCommentId(), false);
            if( isChk == false ){
                throw new NotFoundException("Parent Comment not found", commentCreationDTO.getParentCommentId());
            }
        }

        Comments comment = new Comments();
        comment.setPosts(post);
        comment.setUserEntity(authUser);
        comment.setContent(commentCreationDTO.getContent());
        comment.setParentCommentId(commentCreationDTO.getParentCommentId() != null ?commentCreationDTO.getParentCommentId():0);
        comment.setCreateAt(LocalDateTime.now());
        comment.setDeleteStatus(false);
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 등록 완료", comment.getCommentId() );
    }

    public ResponseDto modifyComment(CustomUserDetails customUserDetails, CommentModifyDTO commentModifyDTO) {
        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found", customUserDetails.getUserId()));

        Posts post = postsJpa.findById(commentModifyDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found", commentModifyDTO.getPostId()));

        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentModifyDTO.getCommentId(), false);
        if (comment == null) {
            throw new NotFoundException("Comment not found", commentModifyDTO.getCommentId());
        }

        comment.setCommentId(commentModifyDTO.getCommentId());
        comment.setPosts(post);
        comment.setUserEntity(authUser);
        comment.setContent(commentModifyDTO.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 수정 완료,", comment.getCommentId());
    }


    public ResponseDto deleteComment(CustomUserDetails customUserDetails, Integer commentId) {
        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("User not found", customUserDetails.getUserId()));

        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentId, false);
        if( comment == null ){
            throw new NotFoundException("Comment not found", commentId);
        }

        if( comment.getDeleteStatus() ) {
            throw new BadRequestException("Already NotFound Comment", commentId);
        }

        comment.setDeleteStatus(true);
        comment.setDeleteAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 삭제 완료", comment.getCommentId() );
    }

    public ResponseDto toggleCommentLike(CustomUserDetails customUserDetails, Integer commentId) {
        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("User not found", customUserDetails.getUserId()));
        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentId, false);
        if( comment == null ){
            throw new NotFoundException("Comment not found", commentId);
        }

        if( comment.getDeleteStatus() ) {
            throw new BadRequestException("Already NotFound Comment", commentId);
        }

        CommentFavorite commentFavorite = commentFavoriteJpa.findByUserEntityAndComments(authUser,comment);
        if( commentFavorite == null ) {
            CommentFavorite newCommentFavorite = new CommentFavorite();
            newCommentFavorite.setComments(comment);
            newCommentFavorite.setUserEntity(authUser);
            commentFavoriteJpa.save(newCommentFavorite);
            return new ResponseDto(200, "댓글 좋아요 완료 ", commentId );
        } else {
            commentFavoriteJpa.deleteByUserEntityAndComments(authUser, comment);
            return new ResponseDto(200, "댓글 좋아요 취소 완료 ", commentId );
        }
    }
}
