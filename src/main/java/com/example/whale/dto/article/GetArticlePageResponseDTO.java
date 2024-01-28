package com.example.whale.dto.article;

import com.example.whale.dto.user.WriterResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetArticlePageResponseDTO {

    private final Long articleId;
    private final String title;
    private final WriterResponseDTO writer;
    private final int commentCount;
    private final int heartCount;

    @Builder
    @QueryProjection
    public GetArticlePageResponseDTO(Long articleId, String title, WriterResponseDTO writer, int commentCount, int heartCount) {
        this.articleId = articleId;
        this.title = title;
        this.writer = writer;
        this.commentCount = commentCount;
        this.heartCount = heartCount;
    }

    public static GetArticlePageResponseDTO of(Long articleId, String title, WriterResponseDTO writer, int commentCount, int heartCount) {
        return GetArticlePageResponseDTO.builder()
                                        .articleId(articleId)
                                        .title(title)
                                        .writer(writer)
                                        .commentCount(commentCount)
                                        .heartCount(heartCount)
                                        .build();
    }

}
