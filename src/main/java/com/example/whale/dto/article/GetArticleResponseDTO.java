package com.example.whale.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.dto.attachment.AttachmentToResource;
import com.example.whale.dto.attachment.GetAttachmentResponseDTO;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.util.FileConverter;
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
    @JsonIgnore
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
