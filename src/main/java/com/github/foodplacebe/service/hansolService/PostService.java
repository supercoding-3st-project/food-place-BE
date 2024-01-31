package com.github.foodplacebe.service.hansolService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.hansolService.hansolMappers.PostMapper;
import com.github.foodplacebe.web.dto.hansolDto.FindPostsResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostsJpa postsJpa;
    private final PostPhotosJpa postPhotosJpa;
    private final UserJpa userJpa;

    @Transactional(transactionManager = "tm")
    public ResponseDto postRegister(CustomUserDetails customUserDetails, PostRegisterRequest postRegisterRequest) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("유저정보를 찾을 수 없습니다.", customUserDetails.getUserId()));

        String[] categoryArray = {"한식", "중식", "일식", "양식", "카페/베이커리"};
        List<String> categoryList = new ArrayList<>(Arrays.asList(categoryArray));
        if (!categoryList.contains(postRegisterRequest.getCategory()) && !postRegisterRequest.getCategory().equals("전체"))
            throw new BadRequestException("유효하지 않은 카테고리입니다.", postRegisterRequest.getCategory());


        String address = postRegisterRequest.getAddress();
        String postNeighborhood;
        if (address.contains("서울특별시")) postNeighborhood = "서울";
        else if (address.contains("경기도") || address.contains("인천광역시")) postNeighborhood = "경기/인천";
        else if (address.contains("부산광역시") || address.contains("경상남도")) postNeighborhood = "부산/경남";
        else if (address.contains("대구광역시") || address.contains("경상북도")) postNeighborhood = "대구/경북";
        else if (address.contains("대전광역시") || address.contains("충청")) postNeighborhood = "대전/충청";
        else if (address.contains("광주시") || address.contains("전라")) postNeighborhood = "광주/전라";
        else if (address.contains("제주특별자치도")) postNeighborhood = "제주";
        else if (address.contains("강원특별자치도")) postNeighborhood = "강원";
        else throw new BadRequestException("주소지 오류", address);

        Posts post = PostMapper.INSTANCE.postRegisterRequestToPosts(postRegisterRequest, userEntity, postNeighborhood);
        postsJpa.save(post);

        Integer postId = post.getPostId();

        if(postRegisterRequest.getPostPhotos().stream().count() > 4)
            throw new BadRequestException("맛집 사진은 4개까지만 등록 가능합니다.", "photo: " + postRegisterRequest.getPostPhotos().stream().count() + "개");

        postRegisterRequest.getPostPhotos().forEach(
                (ppr) -> {
                    PostPhotos postPhotos = PostPhotos.builder()
                            .posts(post)
                            .photo(ppr)
                            .build();
                    postPhotosJpa.save(postPhotos);
                }
        );

        return new ResponseDto(200, "게시물 등록 성공", "postId: " + postId);

    }

    public ResponseDto findPosts(String area, String category, String order, Pageable pageable) { // 지역, 카테고리 별 조회(최신순, 인기순, 조회순)
        String[] areaArray = {"서울", "경기/인천", "부산/경남", "대구/경북", "대전/충청", "광주/전라", "제주", "강원"};
        String[] categoryArray = {"한식", "중식", "일식", "양식", "카페/베이커리"};
        String[] orderArray = {"최신순", "인기순", "조회순"};

        List<String> areaList = new ArrayList<>(Arrays.asList(areaArray));
        List<String> categoryList = new ArrayList<>(Arrays.asList(categoryArray));
        List<String> oderList = new ArrayList<>(Arrays.asList(orderArray));

        if (!areaList.contains(area) && !area.equals("전체")) throw new BadRequestException("유효하지 않은 지역명입니다.", area);
        if (!categoryList.contains(category) && !category.equals("전체")) throw new BadRequestException("유효하지 않은 카테고리입니다.", category);
        if (!oderList.contains(order)) throw new BadRequestException("유효하지 않은 순서입니다.", order);

        List<String> filteredAreaList;
        List<String> filteredCategoryList;

        if (!area.equals("전체")) filteredAreaList = areaList.stream().filter(a -> a.equals(area)).collect(Collectors.toList());
        else filteredAreaList = areaList;
        if (!category.equals("전체")) filteredCategoryList = categoryList.stream().filter(a -> a.equals(category)).collect(Collectors.toList());
        else filteredCategoryList = categoryList;

        Page<FindPostsResponse> findPostsResponse;
        if (order.equals("최신순")) findPostsResponse = postsJpa.findPostsByAreaAndCategoryAndOrderOrderByCreateAt(filteredAreaList, filteredCategoryList, pageable);
        else if (order.equals("인기순")) findPostsResponse = postsJpa.findPostsByAreaAndCategoryAndOrderOrderByFavoriteCount(filteredAreaList, filteredCategoryList, pageable);
        else findPostsResponse = postsJpa.findPostsByAreaAndCategoryAndOrderOrderByViewCount(filteredAreaList, filteredCategoryList, pageable);

        return new ResponseDto(200, "맛집 게시물 조회 성공", findPostsResponse);
    }

    public ResponseDto searchPosts(String keyword, Pageable pageable) { // 게시물 이름(name), 메뉴(menu), 주소(address)로 검색
        Page<FindPostsResponse> findPostsResponses = postsJpa.findPostsByKeywordByFavoriteCount(keyword, pageable);

        return new ResponseDto(200, "키워드 검색 성공/ KEYWORD: " + keyword, findPostsResponses);
    }
}
