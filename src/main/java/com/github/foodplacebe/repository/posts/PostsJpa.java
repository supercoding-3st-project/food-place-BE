package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsJpa extends JpaRepository<Posts, Integer> {

    Posts findByUserEntityAndPostId(UserEntity userEntity,Integer postId);
}
