package com.example.whale.dto.article;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetArticleResponseDTO {

    private final Long articleId;
    private final String writer;
    private final String title;
    private final String content;
    private List<GetCommentResponseDTO> comments;

    @Builder
    @QueryProjection
    public GetArticleResponseDTO(Long articleId, String writer, String title, String content) {
        this.articleId = articleId;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public static final GetArticleResponseDTO from(ArticleEntity articleEntity) {
        return GetArticleResponseDTO.builder()
                                    .articleId(articleEntity.getId())
                                    .writer(articleEntity.getWriter().getNickname())
                                    .title(articleEntity.getTitle())
                                    .content(articleEntity.getContent())
                                    .build();
    }

    public void setCommentsInArticle(List<GetCommentResponseDTO> commentsInArticle) {
        this.comments = commentsInArticle;
    }

}
