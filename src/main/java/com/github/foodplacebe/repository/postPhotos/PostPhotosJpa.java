package com.github.foodplacebe.repository.postPhotos;

import com.github.foodplacebe.repository.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPhotosJpa extends JpaRepository<PostPhotos, Integer> {
    void deleteAllByPosts(Posts posts);
}
