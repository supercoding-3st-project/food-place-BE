package com.github.foodplacebe.service.eunjiService;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.postPhotos.PostPhotosJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.web.dto.postDto.PostRequest;
import com.github.foodplacebe.web.dto.postDto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final PostPhotosJpa postPhotosJpa;
    public void checkCategory(PostRequest postRequest){
        String[] categoryArray = {"한식", "중식", "일식", "양식", "카페", "베이커리"};
        List<String> categoryList = new ArrayList<>(Arrays.asList(categoryArray));

        if (!categoryList.contains(postRequest.getCategory()) && !postRequest.getCategory().equals("전체"))
            throw new BadRequestException("유효하지 않은 카테고리입니다.", postRequest.getCategory());
    }

    public String determineNeighborhood(String address){
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

        return postNeighborhood;
    }

    public PostResponse response(Posts posts){
        List<PostPhotos> postPhotos = postPhotosJpa.findByPosts(posts);
        if(!postPhotos.isEmpty()){
            posts.setMainPhoto(postPhotos.get(0).getPhoto());
        }
        return new PostResponse(posts, postPhotos);
    }
}
