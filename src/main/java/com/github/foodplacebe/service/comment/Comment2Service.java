package com.github.foodplacebe.service.comment;

import com.github.foodplacebe.repository.commentFavorite.CommentFavorite;
import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.AccessDenied;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.web.dto.comment.CommentCreationDTO;
import com.github.foodplacebe.web.dto.comment.CommentModifyDTO;
import com.github.foodplacebe.web.dto.comment.CommentResponse_AuthDTO;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.reactor.ReactorEnvironmentPostProcessor;
import org.springframework.stereotype.Service;
import com.github.foodplacebe.repository.commentFavorite.CommentFavoriteJpa;
import com.github.foodplacebe.repository.comments.CommentsJpa;
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

    public ResponseDto getCommentsByPostId(CustomUserDetails customUserDetails, Integer postId, Pageable pageable){
        if( customUserDetails != null ){
            Integer userId = customUserDetails.getUserId();
            Page<CommentResponse_AuthDTO> response = commentsJpa.findByPostsPostIdAndParentCommentIdAndDeleteStatusOrderByCreateAt(postId, 0, false, pageable)
                    .map(comment -> convertToCommentResponse_AuthDTO(comment, userId));

            if (response.getTotalElements() == 0) {
                return new ResponseDto(404, "NOT_FOUND", "해당 게시글에 댓글이 없습니다.");
            }
            return new ResponseDto(200, "댓글 조회(게시물) 완료", response );
        }
        else {
            Page<CommentResponseDTO> response = commentsJpa.findByPostsPostIdAndParentCommentIdAndDeleteStatusOrderByCreateAt(postId, 0, false, pageable)
                    .map(this::convertToCommentResponseDTO);

            if (response.getTotalElements() == 0) {
                return new ResponseDto(404, "NOT_FOUND", "해당 게시글에 댓글이 없습니다.");
            }
            return new ResponseDto(200, "댓글 조회(게시물) 완료", response );
        }
    }



    public ResponseDto getCommentsByCommentId(CustomUserDetails customUserDetails, Integer commentId, Pageable pageable){
        if( customUserDetails != null ){
            Integer userId = customUserDetails.getUserId();
            Page<CommentResponse_AuthDTO> response = commentsJpa.findByParentCommentIdAndDeleteStatusOrderByCreateAt(commentId, false, pageable)
                    .map(comment -> convertToCommentResponse_AuthDTO(comment, userId));

            if (response.getTotalElements() == 0) {
                return new ResponseDto(404, "NOT_FOUND", "해당 댓글에 댓글이 없습니다.");
            }
            return new ResponseDto(200, "댓글 조회(댓글) 완료", response);
        } else {
            Page<CommentResponseDTO> response = commentsJpa.findByParentCommentIdAndDeleteStatusOrderByCreateAt(commentId, false, pageable)
                    .map(this::convertToCommentResponseDTO);

            if (response.getTotalElements() == 0) {
                return new ResponseDto(404, "NOT_FOUND", "해당 댓글에 댓글이 없습니다.");
            }
            return new ResponseDto(200, "댓글 조회(댓글) 완료", response);
        }
    }

    private CommentResponseDTO convertToCommentResponseDTO(Comments comment){
        String profileImg = comment.getUserEntity().getImageUrl();
        int likeCount = commentFavoriteJpa.countByCommentsCommentId(comment.getCommentId());
        return new CommentResponseDTO(
                comment.getPosts().getPostId(),
                comment.getUserEntity().getUserId(),
                comment.getCommentId(),
                comment.getParentCommentId(),
                comment.getContent(),
                likeCount,
                profileImg,
                comment.getCreateAt(),
                comment.getUpdateAt()
        );
    }


    private CommentResponse_AuthDTO convertToCommentResponse_AuthDTO(Comments comment, Integer userId) {
        int likeCount = commentFavoriteJpa.countByCommentsCommentId(comment.getCommentId());
        boolean isLiked = commentFavoriteJpa.existsByUserEntityUserIdAndCommentsCommentId(userId, comment.getCommentId());
        String profileImg = comment.getUserEntity().getImageUrl();
        return new CommentResponse_AuthDTO(
                comment.getPosts().getPostId(),
                comment.getUserEntity().getUserId(),
                comment.getCommentId(),
                comment.getParentCommentId(),
                comment.getContent(),
                likeCount,
                comment.getCreateAt(),
                comment.getUpdateAt(),
                profileImg,
                isLiked
        );
    }

    public ResponseDto addComment(CustomUserDetails customUserDetails, CommentCreationDTO commentCreationDTO) {
        if(customUserDetails == null ){
            throw new AccessDenied("로그인을 해주세요.", "");
        }

        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("유저정보를 찾을 수 없습니다.", ""));

        Posts post = postsJpa.findById(commentCreationDTO.getPostId())
                .orElseThrow(()->new NotFoundException("게시물을 찾을 수 없습니다.", "postId : " + commentCreationDTO.getPostId()));

        if( commentCreationDTO.getParentCommentId() != null ) {
            boolean isChk = commentsJpa.existsByCommentIdAndDeleteStatus(commentCreationDTO.getParentCommentId(), false);
            if( isChk == false ){
                throw new NotFoundException("부모 댓글을 찾을 수 없습니다.", "commentId : " + commentCreationDTO.getParentCommentId());
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

        return new ResponseDto(200, "댓글 등록 완료", "commentId : "+comment.getCommentId() );
    }

    public ResponseDto modifyComment(CustomUserDetails customUserDetails, CommentModifyDTO commentModifyDTO) {
        if(customUserDetails == null ){
            throw new AccessDenied("로그인을 해주세요.", "");
        }

        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다.", ""));

        Posts post = postsJpa.findById(commentModifyDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.", "postId : " + commentModifyDTO.getPostId()));

        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentModifyDTO.getCommentId(), false);
        if (comment == null) {
            throw new NotFoundException("댓글을 찾을 수 없습니다.", commentModifyDTO.getCommentId());
        }

        comment.setCommentId(commentModifyDTO.getCommentId());
        comment.setPosts(post);
        comment.setUserEntity(authUser);
        comment.setContent(commentModifyDTO.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 수정 완료", "commentId : " + comment.getCommentId());
    }


    public ResponseDto deleteComment(CustomUserDetails customUserDetails, Integer commentId) {
        if(customUserDetails == null ){
            throw new AccessDenied("로그인을 해주세요.", "");
        }

        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("유저정보를 찾을 수 없습니다.", ""));

        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentId, false);
        if( comment == null ){
            throw new NotFoundException("이미 삭제된 댓글입니다.", "commentId : " + commentId);
        }

        if( comment.getDeleteStatus() ) {
            throw new NotFoundException("이미 삭제된 댓글입니다.", "commentId : " + commentId);
        }

        comment.setDeleteStatus(true);
        comment.setDeleteAt(LocalDateTime.now());
        commentsJpa.save(comment);

        return new ResponseDto(200, "댓글 삭제 완료", "commentId : " + comment.getCommentId() );
    }

    public ResponseDto toggleCommentLike(CustomUserDetails customUserDetails, Integer commentId) {
        if(customUserDetails == null ){
            throw new AccessDenied("로그인을 해주세요.", "");
        }

        UserEntity authUser = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("유저정보를 찾을 수 없습니다.", ""));
        Comments comment = commentsJpa.findByCommentIdAndDeleteStatus(commentId, false);
        if( comment == null ){
            throw new NotFoundException("댓글을 찾을 수 없습니다.", "commentId : " + commentId);
        }

        if( comment.getDeleteStatus() ) {
            throw new NotFoundException("이미 삭제된 댓글입니다.", "commentId : " + commentId);
        }

        CommentFavorite commentFavorite = commentFavoriteJpa.findByUserEntityAndComments(authUser,comment);
        if( commentFavorite == null ) {
            CommentFavorite newCommentFavorite = new CommentFavorite();
            newCommentFavorite.setComments(comment);
            newCommentFavorite.setUserEntity(authUser);
            commentFavoriteJpa.save(newCommentFavorite);
            return new ResponseDto(200, "댓글 좋아요 완료", "commentId : " + commentId );
        } else {
            commentFavoriteJpa.deleteById(commentFavorite.getCommentFavoriteId());
            return new ResponseDto(200, "댓글 좋아요 취소 완료", "commentId : " + commentId );
        }
    }
}
