package com.example.whale.domain.article.dto;

import com.example.whale.domain.article.entity.ArticleEntity;

import lombok.Builder;
import lombok.Getter;

public class CreateArticleDTO {

    @Getter
    public static class CreateArticleRequestDTO {
        private String title;
        private String content;
    }

    @Getter
    public static class CreateArticleResponseDTO {
        private final Long articleId;
        private final String writer;
        private final String title;
        private final String content;

        @Builder
        public CreateArticleResponseDTO(Long articleId, String writer, String title, String content) {
            this.articleId = articleId;
            this.writer = writer;
            this.title = title;
            this.content = content;
        }

        public static CreateArticleResponseDTO from(ArticleEntity articleEntity) {
            return CreateArticleResponseDTO.builder()
                                            .articleId(articleEntity.getId())
                                            .writer(articleEntity.getWriter().getNickname())
                                            .title(articleEntity.getTitle())
                                            .content(articleEntity.getContent())
                                            .build();
        }
    }

}
