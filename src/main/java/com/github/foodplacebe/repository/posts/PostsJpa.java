package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
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
    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites)) " +
                    "FROM Posts p " +
                    "WHERE p.neighborhood IN ?1 AND p.category IN ?2 " +
                    "GROUP BY p.postId " +
                    "ORDER BY p.createAt DESC"
    )
    Page<FindPostsResponse> findPostsByAreaAndCategoryAndOrderOrderByCreateAt(List<String> areaList, List<String> categoryList, Pageable pageable);


    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites)) " +
                    "FROM Posts p " +
                    "WHERE p.neighborhood IN ?1 AND p.category IN ?2 " +
                    "GROUP BY p.postId " +
                    "ORDER BY SIZE(p.postFavorites) DESC, p.createAt DESC "
    )
    Page<FindPostsResponse> findPostsByAreaAndCategoryAndOrderOrderByFavoriteCount(List<String> areaList, List<String> categoryList, Pageable pageable);

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites)) " +
                    "FROM Posts p " +
                    "WHERE p.neighborhood IN ?1 AND p.category IN ?2 " +
                    "GROUP BY p.postId " +
                    "ORDER BY p.viewCount DESC, p.createAt DESC "
    )
    Page<FindPostsResponse> findPostsByAreaAndCategoryAndOrderOrderByViewCount(List<String> areaList, List<String> categoryList, Pageable pageable);

    Page<FindPostsResponse> findTopByOrderByViewCountDesc(int count, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.address LIKE CONCAT('%', :city, '%')")
    Page<Posts> findRestaurantsByCity(@Param("city") String city, Pageable pageable);


}
