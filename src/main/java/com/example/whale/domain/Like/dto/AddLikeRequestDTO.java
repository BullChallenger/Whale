package com.example.whale.domain.Like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddLikeRequestDTO {

    private final Long userId;
    private final Long articleId;

}
