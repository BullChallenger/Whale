package com.example.whale.domain.attachment.dto;

import java.io.IOException;
import java.io.Serializable;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetAttachmentResponseDTO implements Serializable {

    private final String attachmentId;

    private final String fileOriginName;

    private final String filePath;

    private final String fileExtension;

    private final String contentType;

    @Builder
    @QueryProjection
    public GetAttachmentResponseDTO(String attachmentId, String fileOriginName, String filePath, String fileExtension,
                                    String contentType) throws IOException {
        this.attachmentId = attachmentId;
        this.fileOriginName = fileOriginName;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }

    public static GetAttachmentResponseDTO from(
            String attachmentId,
            String fileOriginName,
            String filePath,
            String fileExtension,
            String contentType
    ) throws IOException {
        return GetAttachmentResponseDTO.builder()
                                        .attachmentId(attachmentId)
                                        .fileOriginName(fileOriginName)
                                        .filePath(filePath)
                                        .fileExtension(fileExtension)
                                        .contentType(contentType)
                                        .build();
    }

}