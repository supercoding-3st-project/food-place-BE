package com.github.foodplacebe.service;


import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final PostsJpa postsJpa;
    private final UserJpa userJpa;


    public Page<FindPostsResponse> getTopPostsByViewCount(int count, Pageable pageable) {
        Page<FindPostsResponse> topByOrderByViewCountDesc = postsJpa.findTopByOrderByViewCountDesc(count, pageable);
        return topByOrderByViewCountDesc;
    }
}
