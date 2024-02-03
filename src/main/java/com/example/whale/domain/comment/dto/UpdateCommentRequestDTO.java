package com.example.whale.domain.comment.dto;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDTO {

    private Long commentId;
    private String content;

}
