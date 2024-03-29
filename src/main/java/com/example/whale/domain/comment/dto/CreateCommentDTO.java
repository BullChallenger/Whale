package com.example.whale.domain.comment.dto;

import java.util.List;

import com.example.whale.domain.comment.entity.CommentEntity;

import lombok.Builder;
import lombok.Getter;

public class CreateCommentDTO {

    @Getter
    public static class CreateCommentRequestDTO {
        private Long parentCommentId;
        private String content;
        private Long articleId;
    }

    @Getter
    public static class CreateCommentResponseDTO {
        private final Long commentId;
        private final String writer;
        private final String content;
        private final int depth;
        private final GetOneCommentDTO parentComment;
        private final List<CommentEntity> childComments;

        @Builder
        public CreateCommentResponseDTO(Long commentId, String writer, String content, int depth, CommentEntity parentComment, List<CommentEntity> childComments) {
            this.commentId = commentId;
            this.writer = writer;
            this.content = content;
            this.depth = depth;
            this.parentComment = GetOneCommentDTO.from(parentComment);
            this.childComments = childComments;
        }

        public static final CreateCommentResponseDTO from(CommentEntity commentEntity) {
            return CreateCommentResponseDTO.builder()
                    .commentId(commentEntity.getId())
                    .writer(commentEntity.getWriter().getNickname())
                    .content(commentEntity.getContent())
                    .depth(commentEntity.getDepth())
                    .parentComment(commentEntity.getParentComment())
                    .childComments(commentEntity.getChildComments())
                    .build();
        }
    }

}
