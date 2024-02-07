package com.github.foodplacebe.repository.comments;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.web.dto.hansolDto.CommentResponse;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsJpa extends JpaRepository<Comments, Integer> {
    void deleteAllByPosts(Posts posts);

    @Query("SELECT new com.github.foodplacebe.web.dto.hansolDto.CommentResponse(c.commentId, c.posts.postId, c.userEntity.userId, c.content, c.parentCommentId, c.deleteStatus, c.createAt, c.updateAt, c.deleteAt, SIZE(c.commentFavorites) , c.userEntity.nickName, c.userEntity.neighborhood, c.userEntity.gender, c.userEntity.imageUrl) " +
            "FROM Comments c " +
            "WHERE c.posts.postId = ?1 AND c.parentCommentId = 0 " +
            "ORDER BY c.commentId ASC ")
    Page<CommentResponse> findAllComments(Integer postId, Pageable pageable);

    @Query("SELECT new com.github.foodplacebe.web.dto.hansolDto.CommentResponse(c.commentId, c.posts.postId, c.userEntity.userId, c.content, c.parentCommentId, c.deleteStatus, c.createAt, c.updateAt, c.deleteAt, SIZE(c.commentFavorites) , c.userEntity.nickName, c.userEntity.neighborhood, c.userEntity.gender, c.userEntity.imageUrl) " +
            "FROM Comments c " +
            "WHERE c.posts.postId = ?1 AND c.parentCommentId = ?2 " +
            "ORDER BY c.commentId ASC ")
    Page<CommentResponse> findAllCommentsByCommentId(Integer postId, Integer parentCommentId, Pageable pageable);

    boolean existsByCommentIdAndDeleteStatus(Integer parentCommentId, boolean deleteStatus);

    Comments findByCommentIdAndDeleteStatus(Integer commentId, boolean deleteStatus);

    Page<Comments> findByParentCommentIdAndDeleteStatusOrderByCreateAt(Integer commentId, boolean deleteStatus, Pageable pageable);

    Page<Comments> findByPostsPostIdAndParentCommentIdAndDeleteStatusOrderByCreateAt(Integer postId, int i, boolean b, Pageable pageable);
}
