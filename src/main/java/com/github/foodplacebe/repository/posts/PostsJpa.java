package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.posts.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsJpa extends JpaRepository<Posts, Integer> {

    Posts findByUserEntityAndPostId(UserEntity userEntity,Integer postId);

    @Query("SELECT p FROM Posts p WHERE p.address LIKE CONCAT('%', :city, '%')")
    Page<Posts> findRestaurantsByCity(@Param("city") String city, Pageable pageable);

  
    Page<Posts> findByAddressContaining(String address, Pageable pageable);

    Page<Posts> findByMenuContaining(String menu, Pageable pageable);

    Page<Posts> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.address LIKE %:keyword% OR p.menu LIKE %:keyword% OR p.name LIKE %:keyword%")
    Page<Posts> findByAddressOrMenuOrNameContaining(String keyword, Pageable pageable);

    List<Posts> findByUserEntity(UserEntity userEntity);

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.posts.PostResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites), pf.userEntity.userId, 'Y' as favoriteYn) " +
                    "FROM Posts p " +
                    "LEFT JOIN p.postFavorites pf " +
                    "ON (:userId <> 0 AND p.postId = pf.posts.postId AND pf.userEntity.userId = :userId) " +
                    "WHERE (:neighborhood is null OR p.neighborhood = :neighborhood OR :neighborhood = '') " +
                    "AND (:category is null OR p.category = :category OR :category = '') " +
                    "ORDER BY " +
                    "   CASE WHEN :orderParam = '최신순' THEN p.createAt " +
                    "        WHEN :orderParam = '조회순' THEN p.viewCount " +
                    "        WHEN :orderParam = '인기순' THEN SIZE(p.postFavorites) END DESC "
    )
    Page<PostResponse> findPostsListAndFavorite(int userId, String neighborhood, String category, String orderParam, Pageable pageable);

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites)) " +
                    "FROM Posts p " +
                    "WHERE p.address = ?1 AND p.name = ?2 AND p.postId != ?3 " +
                    "GROUP BY p.postId "
    )
    List<FindPostsResponse> findFiveRelatedPosts(String address, String name, Integer postId);
}

