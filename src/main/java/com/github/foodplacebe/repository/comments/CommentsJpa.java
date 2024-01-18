package com.github.foodplacebe.repository.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsJpa extends JpaRepository<Comments, Integer> {
}
