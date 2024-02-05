package com.example.whale.domain.article.dto;

import com.example.whale.domain.user.dto.WriterResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetArticlePageResponseDTO {

    private final Long articleId;
    private final String title;
    private final WriterResponseDTO writer;
    private final int commentCount;
    private Long likeCount;

    @Builder
    @QueryProjection
    public GetArticlePageResponseDTO(Long articleId, String title, WriterResponseDTO writer, int commentCount) {
        this.articleId = articleId;
        this.title = title;
        this.writer = writer;
        this.commentCount = commentCount;
    }

    public static GetArticlePageResponseDTO of(Long articleId, String title, WriterResponseDTO writer, int commentCount) {
        return GetArticlePageResponseDTO.builder()
                                        .articleId(articleId)
                                        .title(title)
                                        .writer(writer)
                                        .commentCount(commentCount)
                                        .build();
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
