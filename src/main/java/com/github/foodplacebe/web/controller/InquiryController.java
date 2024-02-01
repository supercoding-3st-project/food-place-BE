package com.github.foodplacebe.web.controller;


import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.InquiryService;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

//    @GetMapping("/top")
//    public ResponseEntity<Page<FindPostsResponse>> getTopPosts(@RequestParam(name = "count", defaultValue = "10") int count,
//                                                               @PageableDefault(size = 10) Pageable pageable) {
//        Page<FindPostsResponse> topPosts = inquiryService.getTopPostsByViewCount(count, pageable);
//        return ResponseEntity.ok(topPosts);
//    }

    @GetMapping("/liked")
    public List<FindPostsResponse> getLikedPosts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<FindPostsResponse> likedPosts = inquiryService.getFavoritePostsByUserId(customUserDetails);
        return likedPosts;
    }

    @GetMapping("/neighborhood")
    public List<FindPostsResponse> findRestaurants(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<FindPostsResponse> restaurants = inquiryService.findRestaurantsByUserNeighborhood(customUserDetails);
        return restaurants;
    }

}
