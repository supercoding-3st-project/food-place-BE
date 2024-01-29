package com.github.foodplacebe.service.PostsService;

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


@Service
@RequiredArgsConstructor
public class SearchPostService {
    private final PostsJpa postsJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto findAddress(String address, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByAddressContaining(address, pageable);

        Page<SearchResponse> searchResponses = postsList.map(posts -> new SearchResponse(posts));

        List<SearchResponse> searchResponseList = searchResponses.getContent();

        return new ResponseDto(200,"주소로 검색 완료", "address : " + searchResponseList);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findMenu(String menu, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByMenuContaining(menu, pageable);

        Page<SearchResponse> searchResponses = postsList.map(posts -> new SearchResponse(posts));

        List<SearchResponse> searchResponseList = searchResponses.getContent();

        return new ResponseDto(200,"메뉴로 검색 완료", "menu : " + searchResponseList);
    }

    @Transactional(transactionManager = "tm")
    public ResponseDto findName(String name, Pageable pageable) {
        Page<Posts> postsList = postsJpa.findByNameContaining(name, pageable);

        Page<SearchResponse> searchResponses = postsList.map(posts -> new SearchResponse(posts));

        List<SearchResponse> searchResponseList = searchResponses.getContent();

        return new ResponseDto(200,"이름으로 검색 완료", "name : " + searchResponseList);
    }
}
