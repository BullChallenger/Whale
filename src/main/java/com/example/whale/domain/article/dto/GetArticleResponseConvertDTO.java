package com.example.whale.domain.article.dto;

import com.example.whale.domain.attachment.dto.AttachmentToResource;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;
import com.example.whale.domain.comment.dto.GetCommentResponseDTO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetArticleResponseConvertDTO implements Serializable {

    private Long articleId;
    private String articleWriter;
    private String articleTitle;
    private String articleContent;

    private List<GetCommentResponseDTO> comments = new ArrayList<>();
    private List<AttachmentToResource> attachments = new ArrayList<>();

    private Long likeCount;

    @Builder
    public GetArticleResponseConvertDTO(Long articleId, String articleWriter, String articleTitle, String articleContent) {
        this.articleId = articleId;
        this.articleWriter = articleWriter;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
    }

    public static GetArticleResponseConvertDTO from(GetArticleResponseDTOV2 dto) {
        return GetArticleResponseConvertDTO.builder()
                .articleId(dto.getArticleId())
                .articleWriter(dto.getArticleWriter())
                .articleTitle(dto.getArticleTitle())
                .articleContent(dto.getArticleContent())
                .build();
    }

    public void convert(GetArticleResponseDTOV2 dto) {
        if (dto.getCommentId() != null) {
            addCommentInDTO(GetCommentResponseDTO
                    .builder()
                    .commentId(dto.getCommentId())
                    .writer(dto.getCommentWriter())
                    .content(dto.getCommentContent())
                    .depth(dto.getCommentDepth())
                    .parentCommentId(dto.getParentCommentId())
                    .build()
            );
        }

        if (dto.getAttachmentId() != null) {
            try {
                addAttachmentInDTO(GetAttachmentResponseDTO
                        .builder()
                        .attachmentId(dto.getAttachmentId())
                        .fileOriginName(dto.getFileOriginName())
                        .filePath(dto.getFilePath())
                        .fileExtension(dto.getFileExtension())
                        .contentType(dto.getContentType())
                        .build()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCommentInDTO(GetCommentResponseDTO dto) {
        this.comments.add(dto);
    }

    public void addAttachmentInDTO(GetAttachmentResponseDTO dto) {
        this.attachments.add(AttachmentToResource.from(dto));
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

}
