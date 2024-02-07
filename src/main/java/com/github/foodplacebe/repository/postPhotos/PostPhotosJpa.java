package com.github.foodplacebe.repository.postPhotos;

import com.github.foodplacebe.repository.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostPhotosJpa extends JpaRepository<PostPhotos, Integer> {
    void deleteAllByPosts(Posts posts);

    List<PostPhotos> findByPosts(Posts posts);

    @Query("SELECT pp.photo FROM PostPhotos pp WHERE pp.posts.postId = :postId")
    List<String> findByPostsPostId(Integer postId);
    @Transactional
    void deleteByPhoto(String photo);


}
