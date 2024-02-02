package com.github.foodplacebe.service.post;

import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.web.dto.posts.PostRequest;
import com.github.foodplacebe.web.dto.posts.PostResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service("postService")
@RequiredArgsConstructor
public class PostService {
    private final PostsJpa postsJpa;

    public ResponseDto getPostList(PostRequest postRequest, Pageable pageable) {

        Page<PostResponse> findPostList;
        findPostList = postsJpa.findPostsList(postRequest.getNeighborhood(), postRequest.getCategory(), postRequest.getOrder(), pageable);

        return new ResponseDto(200, "맛집 리스트 조회 성공", findPostList);
    }

}
