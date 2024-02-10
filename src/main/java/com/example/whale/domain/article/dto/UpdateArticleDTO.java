package com.example.whale.domain.article.dto;

import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;
import com.example.whale.domain.attachment.entity.AttachmentEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

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
        private final List<GetAttachmentResponseDTO> attachments;

        @Builder
        public UpdateArticleResponseDTO(String title, String content, List<AttachmentEntity> attachments) {
            this.title = title;
            this.content = content;
            this.attachments = attachments.stream().map(this::convertToDTO).toList();
        }

        public static UpdateArticleResponseDTO from(ArticleEntity articleEntity) {
            return UpdateArticleResponseDTO.builder()
                                            .title(articleEntity.getTitle())
                                            .content(articleEntity.getContent())
                                            .attachments(articleEntity.getAttachments().attachments())
                                            .build();
        }

        private GetAttachmentResponseDTO convertToDTO(AttachmentEntity attachment) {
            try {
                return GetAttachmentResponseDTO.from(
                        attachment.getId(),
                        attachment.getFileOriginName(),
                        attachment.getFilePath(),
                        attachment.getFileExtension(),
                        attachment.getContentType()
                );
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
