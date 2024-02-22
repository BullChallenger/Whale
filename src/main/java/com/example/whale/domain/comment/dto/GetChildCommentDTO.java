package com.example.whale.domain.comment.dto;

import com.example.whale.domain.comment.entity.CommentEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetChildCommentDTO {

    private final Long commentId;
    private final Long parentCommentId;
    private final String writer;
    private final String content;
    private final int depth;

    @Builder
    public GetChildCommentDTO(Long commentId, Long parentCommentId, String writer, String content, int depth) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.writer = writer;
        this.content = content;
        this.depth = depth;
    }

    public static GetChildCommentDTO from(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        return GetChildCommentDTO.builder()
                .commentId(commentEntity.getId())
                .parentCommentId(commentEntity.getParentComment().getId())
                .writer(commentEntity.getWriter().getNickname())
                .content(commentEntity.getContent())
                .depth(commentEntity.getDepth())
                .build();
    }

}
