package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.commentFavorite.CommentFavorite;
import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsJpa extends JpaRepository<Posts, Integer> {


}
