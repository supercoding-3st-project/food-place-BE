package com.github.foodplacebe.repository.postFavorite;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFavoriteJpa extends JpaRepository<PostFavorite, Integer> {
    PostFavorite findByUserEntityAndPosts(UserEntity userEntity, Posts posts);

    void deleteAllByPosts(Posts posts);

    void deleteByUserEntityAndPosts(UserEntity userEntity, Posts Posts);

    Integer countByUserEntityAndPosts(UserEntity userEntity, Posts posts);
}
