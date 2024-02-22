package com.example.whale.domain.comment.dto;

import com.example.whale.domain.comment.entity.CommentEntity;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCommentResponseDTO {

    private final Long commentId;
    private final String writer;
    private final String content;
    private final int depth;
    private final Long parentCommentId;

    @Builder
    @QueryProjection
    public GetCommentResponseDTO(Long commentId, String writer, String content, int depth, Long parentCommentId) {
        this.commentId = commentId;
        this.writer = writer;
        this.content = content;
        this.depth = depth;
        this.parentCommentId = parentCommentId;
    }

    public static final GetCommentResponseDTO from(CommentEntity commentEntity) {
        return GetCommentResponseDTO.builder()
                .commentId(commentEntity.getId())
                .writer(commentEntity.getWriter().getNickname())
                .content(commentEntity.getContent())
                .depth(commentEntity.getDepth())
                .parentCommentId(commentEntity.getParentComment().getId())
                .build();
    }

    public static final GetCommentResponseDTO fromByRootComment(CommentEntity commentEntity) {
        return GetCommentResponseDTO.builder()
                .commentId(commentEntity.getId())
                .writer(commentEntity.getWriter().getNickname())
                .content(commentEntity.getContent())
                .depth(commentEntity.getDepth())
                .parentCommentId(null)
                .build();
    }

}
