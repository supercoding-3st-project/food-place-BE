package com.github.foodplacebe.service.PostsService;

import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.web.dto.postDto.SearchResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchPostService {
    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;


    @Transactional(transactionManager = "tm")
    public ResponseDto findAddress(String address, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByAddressContaining(address, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + address);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                searchResponses.add(new SearchResponse(post, likeCount));
            }

            return new ResponseDto(200, "주소로 검색 완료", searchResponses);
        }
    }


    @Transactional(transactionManager = "tm")
    public ResponseDto findMenu(String menu, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByMenuContaining(menu, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + menu);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                searchResponses.add(new SearchResponse(post, likeCount));
            }

            return new ResponseDto(200, "메뉴로 검색 완료", searchResponses);
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findName(String name, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByNameContaining(name, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + name);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                searchResponses.add(new SearchResponse(post, likeCount));
            }

            return new ResponseDto(200, "이름으로 검색 완료", searchResponses);
        }
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findKeyword(String keyword, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByAddressOrMenuOrNameContaining(keyword, pageable);

        if (postsList.isEmpty()) {
            return new ResponseDto(400, "해당하는 포스트가 없습니다.", "address: " + keyword);
        } else {
            List<Posts> allPosts = postsList.getContent();

            List<SearchResponse> searchResponses = new ArrayList<>();

            for (Posts post : allPosts) {
                int likeCount = postFavoriteJpa.countByPostsPostId(post.getPostId());
                searchResponses.add(new SearchResponse(post, likeCount));
            }

            return new ResponseDto(200, "키워드로 검색 완료", searchResponses);
        }
    }
}