package com.example.whale.domain.article.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetArticleResponseDTOV2 {

    private Long articleId;
    private String articleWriter;
    private String articleTitle;
    private String articleContent;

    private Long commentId;
    private String commentWriter;
    private String commentContent;
    private Integer commentDepth;
    private Long parentCommentId;

    private String attachmentId;
    private String fileOriginName;
    private String filePath;
    private String fileExtension;
    private String contentType;

    @QueryProjection
    public GetArticleResponseDTOV2(Long articleId, String articleWriter, String articleTitle, String articleContent,
                                   Long commentId, String commentWriter, String commentContent, Integer commentDepth,
                                   Long parentCommentId, String attachmentId, String fileOriginName, String filePath,
                                   String fileExtension, String contentType) {
        this.articleId = articleId;
        this.articleWriter = articleWriter;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.commentId = commentId;
        this.commentWriter = commentWriter;
        this.commentContent = commentContent;
        this.commentDepth = commentDepth;
        this.parentCommentId = parentCommentId;
        this.attachmentId = attachmentId;
        this.fileOriginName = fileOriginName;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }
}
