package com.github.foodplacebe.web.controller.Posts;

import com.github.foodplacebe.service.PostsService.SearchPostService;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class SearchPostController {
    private final SearchPostService searchPostService;

    private static final Logger log = LoggerFactory.getLogger(SearchPostController.class);
    @Operation(summary = "주소로 검색")
    @GetMapping("/search-address")
    public ResponseDto findAddress(@RequestParam(value="address", required = false) String address,
                                  @RequestParam(value="page", defaultValue = "0") int page,
                                  @RequestParam(value="size", defaultValue = "30") int size){

        log.info("Received request to search for address: {}", address);

        Pageable pageable = PageRequest.of(page, size);
        return searchPostService.findAddress(address, pageable);
    }

    @Operation(summary = "메뉴로 검색")
    @GetMapping("/search-menu")
    public ResponseDto findMenu(@RequestParam(value = "menu", required = false) String menu,
                                @RequestParam(value="page", defaultValue = "0") int page,
                                @RequestParam(value="size", defaultValue = "30") int size){

        log.info("Received request to search for menu: {}", menu);

        Pageable pageable = PageRequest.of(page, size);
        return searchPostService.findMenu(menu,pageable);
    }

    @Operation(summary = "맛집 이름으로 검색")
    @GetMapping("/search-name")
    public ResponseDto findName(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value="page", defaultValue = "0") int page,
                                @RequestParam(value="size", defaultValue = "30") int size){

        log.info("Received request to search for name: {}", name);

        Pageable pageable = PageRequest.of(page, size);
        return searchPostService.findName(name,pageable);
    }

    @Operation(summary = "키워드로 검색")
    @GetMapping("/search")
    public ResponseDto findKeyword(@RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value="page", defaultValue = "0") int page,
                                @RequestParam(value="size", defaultValue = "30") int size){

        log.info("Received request to search for name: {}", keyword);

        Pageable pageable = PageRequest.of(page, size);
        return searchPostService.findKeyword(keyword,pageable);
    }

}
