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

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SearchPostService {
    private final PostsJpa postsJpa;
    private final PostFavoriteJpa postFavoriteJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto findAddress(String address, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByAddressContaining(address, pageable);

        List<SearchResponse> searchResponseList = postsList.stream()
                .map(posts -> {
                    int likeCount = postFavoriteJpa.countByPostsPostId(posts.getPostId());
                    return new SearchResponse(posts, likeCount);
                })
                .collect(Collectors.toList());

        return new ResponseDto(200,"주소로 검색 완료", "address : " + searchResponseList);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findMenu(String menu, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByMenuContaining(menu, pageable);

        List<SearchResponse> searchResponseList = postsList.stream()
                .map(posts -> {
                    int likeCount = postFavoriteJpa.countByPostsPostId(posts.getPostId());
                    return new SearchResponse(posts, likeCount);
                })
                .collect(Collectors.toList());

        return new ResponseDto(200,"메뉴로 검색 완료", "menu : " + searchResponseList);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findName(String name, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByNameContaining(name, pageable);

        List<SearchResponse> searchResponseList = postsList.stream()
                .map(posts -> {
                    int likeCount = postFavoriteJpa.countByPostsPostId(posts.getPostId());
                    return new SearchResponse(posts, likeCount);
                })
                .collect(Collectors.toList());

        return new ResponseDto(200,"이름으로 검색 완료", "name : " + searchResponseList);
    }
}
