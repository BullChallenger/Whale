package com.example.whale.domain.article.dto;

import com.example.whale.domain.attachment.dto.AttachmentToResource;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;
import java.io.Serializable;
import java.util.List;

import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.comment.dto.GetCommentResponseDTO;
import com.example.whale.global.util.FileConverter;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetArticleResponseDTO implements Serializable {

    private final Long articleId;
    private final String writer;
    private final String title;
    private final String content;
    private List<GetCommentResponseDTO> comments;
    private List<AttachmentToResource> attachments;
    private int heartCount;

    @Builder
    @QueryProjection
    public GetArticleResponseDTO(Long articleId, String writer, String title, String content, int heartCount) {
        this.articleId = articleId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.heartCount = heartCount;
    }

    public static final GetArticleResponseDTO from(ArticleEntity articleEntity) {
        return GetArticleResponseDTO.builder()
                                    .articleId(articleEntity.getId())
                                    .writer(articleEntity.getWriter().getNickname())
                                    .title(articleEntity.getTitle())
                                    .content(articleEntity.getContent())
                                    .heartCount(articleEntity.getHearts().size())
                                    .build();
    }

    public void setCommentsInArticle(List<GetCommentResponseDTO> commentsInArticle) {
        this.comments = commentsInArticle;
    }

    public void setAttachmentInArticle(List<GetAttachmentResponseDTO> attachments) {
        this.attachments = FileConverter.responseFile(attachments);
    }

}
