package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsJpa extends JpaRepository<Posts, Integer> {

    List<Posts> findAllByUserEntity(UserEntity userEntity);

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

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, SIZE(p.postFavorites)) " +
                    "FROM Posts p " +
                    "WHERE p.name LIKE %?1% OR p.menu LIKE %?1% OR p.address LIKE %?1% " +
                    "GROUP BY p.postId " +
                    "ORDER BY SIZE(p.postFavorites) DESC, p.createAt DESC "
    )
    Page<FindPostsResponse> findPostsByKeywordByFavoriteCount(String keyword, Pageable pageable);

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "p.postId, p.name, p.neighborhood, p.category, p.menu, p.viewCount, p.mainPhoto, p.createAt, size(p.postFavorites) ) " +
                    "FROM Posts p " +
                    "WHERE p.userEntity.userId = ?1 " +
                    "ORDER BY p.createAt DESC "
    )
    Page<FindPostsResponse> findAllByPostsByPagination(Integer userId, Pageable pageable);

    @Query(
            "SELECT new com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse(" +
                    "pf.posts.postId, pf.posts.name, pf.posts.neighborhood, pf.posts.category, pf.posts.menu, pf.posts.viewCount, pf.posts.mainPhoto, pf.posts.createAt, size(pf.posts.postFavorites) ) " +
                    "FROM PostFavorite pf " +
                    "WHERE pf.userEntity.userId = ?1 " +
                    "ORDER BY pf.postFavoriteId DESC "
    )
    Page<FindPostsResponse> findAllByFavoriteByPagination(Integer userId, Pageable pageable);
}
