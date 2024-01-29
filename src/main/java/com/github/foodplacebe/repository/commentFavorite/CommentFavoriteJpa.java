package com.github.foodplacebe.repository.commentFavorite;

import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentFavoriteJpa extends JpaRepository<CommentFavorite, Integer> {
    CommentFavorite findByUserEntityAndComments(UserEntity userEntity, Comments comments);
    void deleteAllByComments(Comments comments);

    void deleteByUserEntityAndComments(UserEntity userEntity, Comments comments);

    int countByCommentsCommentId(Integer commentId);
}
