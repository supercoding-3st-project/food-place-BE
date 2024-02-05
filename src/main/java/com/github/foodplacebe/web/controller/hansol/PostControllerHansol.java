package com.github.foodplacebe.web.controller.hansol;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
<<<<<<<< HEAD:src/main/java/com/github/foodplacebe/web/controller/hansol/PostController1.java
import com.github.foodplacebe.service.hansolService.PostService1;
========
import com.github.foodplacebe.service.hansolService.PostServiceHansol;
>>>>>>>> f8ba89f4966fdd3b425162354c38f73a11ced65f:src/main/java/com/github/foodplacebe/web/controller/hansol/PostControllerHansol.java
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hansol")
@RequiredArgsConstructor
public class PostControllerHansol {
    private final PostServiceHansol postServiceHansol;
    @PostMapping("/post-register")  // 맛집 게시글 등록
    public ResponseDto postRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PostRegisterRequest postRegisterRequest) {
        return postServiceHansol.postRegister(customUserDetails, postRegisterRequest);
    }

    @GetMapping("/posts") // 맛집 목록 조회
    public ResponseDto findPosts(
            @RequestParam("area") String area,
            @RequestParam("category") String category,
            @RequestParam("order") String order,
            Pageable pageable
    ) {
        return postServiceHansol.findPosts(area, category, order, pageable);
    }

    @GetMapping("/posts-search") // 검색 기능(게시글 이름, 메뉴, 주소)
    public ResponseDto searchPosts(@RequestParam("q") String keyword, Pageable pageable ) {
        return postServiceHansol.searchPosts(keyword, pageable);
    }


}
