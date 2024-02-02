package com.github.foodplacebe.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {
    private String neighborhood;
    private String category;
    private String order;
}
