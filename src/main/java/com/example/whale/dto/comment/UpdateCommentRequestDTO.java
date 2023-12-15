package com.example.whale.dto.comment;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDTO {

    private Long commentId;
    private String content;

}
