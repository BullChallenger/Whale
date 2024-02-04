package com.example.whale.domain.Heart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubLikeRequestDTO {

    private final Long articleId;
    private final Long userId;

}
