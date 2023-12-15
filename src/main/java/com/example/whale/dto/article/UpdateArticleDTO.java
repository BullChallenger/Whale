package com.example.whale.dto.article;

import com.example.whale.domain.ArticleEntity;
import lombok.Builder;
import lombok.Getter;

public class UpdateArticleDTO {

    @Getter
    public static class UpdateArticleRequestDTO {
        private Long articleId;
        private String title;
        private String content;
    }

    @Getter
    public static class UpdateArticleResponseDTO {
        private final String title;
        private final String content;

        @Builder
        public UpdateArticleResponseDTO(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public static UpdateArticleResponseDTO from(ArticleEntity articleEntity) {
            return UpdateArticleResponseDTO.builder()
                                            .title(articleEntity.getTitle())
                                            .content(articleEntity.getContent())
                                            .build();
        }
    }

}
