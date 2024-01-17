package com.github.foodplacebe.repository.postPhotos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPhotosJpa extends JpaRepository<PostPhotos, Integer> {
}
