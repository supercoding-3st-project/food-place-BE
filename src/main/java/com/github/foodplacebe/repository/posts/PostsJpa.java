package com.github.foodplacebe.repository.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsJpa extends JpaRepository<Posts, Integer> {
}
