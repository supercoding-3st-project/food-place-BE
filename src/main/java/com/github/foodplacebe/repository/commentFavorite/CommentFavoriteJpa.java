package com.github.foodplacebe.repository.commentFavorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentFavoriteJpa extends JpaRepository<CommentFavorite, Integer> {
}
