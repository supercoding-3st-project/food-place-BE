package com.github.foodplacebe.web.controller;


import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.InquiryService;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
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
    public ResponseDto getLikedPosts(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     Pageable pageable) {
        ResponseDto likedPosts = inquiryService.getFavoritePostsByUserId(customUserDetails, pageable);
        return likedPosts;
    }

    @GetMapping("/neighborhood")
    public ResponseDto findRestaurants(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   Pageable pageable) {
        ResponseDto restaurants = inquiryService.findRestaurantsByUserNeighborhood(customUserDetails, pageable);
        return restaurants;
    }

}
