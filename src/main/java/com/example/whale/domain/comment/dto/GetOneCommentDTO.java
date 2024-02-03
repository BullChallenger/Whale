package com.example.whale.domain.comment.dto;

import com.example.whale.domain.comment.entity.CommentEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetOneCommentDTO {

    private final Long commentId;
    private final String writer;
    private final String content;
    private final int depth;

    @Builder
    @QueryProjection
    public GetOneCommentDTO(Long commentId, String writer, String content, int depth) {
        this.commentId = commentId;
        this.writer = writer;
        this.content = content;
        this.depth = depth;
    }

    public static GetOneCommentDTO from(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        return GetOneCommentDTO.builder()
                                    .commentId(commentEntity.getId())
                                    .writer(commentEntity.getWriter().getNickname())
                                    .content(commentEntity.getContent())
                                    .depth(commentEntity.getDepth())
                                    .build();
    }

}
