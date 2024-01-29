package com.example.whale.dto.attachment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.io.Serializable;

@Getter
public class GetAttachmentResponseDTO implements Serializable {

    private final String attachmentId;

    private final String fileOriginName;

    private final String filePath;

    private final String fileExtension;

    private final String contentType;

    private final Long size;

    @Builder
    @QueryProjection
    public GetAttachmentResponseDTO(String attachmentId, String fileOriginName, String filePath, String fileExtension,
                                    String contentType, Long size) throws IOException {
        this.attachmentId = attachmentId;
        this.fileOriginName = fileOriginName;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.contentType = contentType;
        this.size = size;
    }

    public static GetAttachmentResponseDTO from(
            String attachmentId,
            String fileOriginName,
            String filePath,
            String fileExtension,
            String contentType,
            Long size
    ) throws IOException {
        return GetAttachmentResponseDTO.builder()
                                        .attachmentId(attachmentId)
                                        .fileOriginName(fileOriginName)
                                        .filePath(filePath)
                                        .fileExtension(fileExtension)
                                        .contentType(contentType)
                                        .size(size)
                                        .build();
    }

}