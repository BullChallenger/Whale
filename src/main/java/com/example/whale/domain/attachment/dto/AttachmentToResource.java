package com.example.whale.domain.attachment.dto;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AttachmentToResource implements Serializable {

    private final byte[] resource;
    private final String encodedFileName;
    private final String extension;
    private final String contentType;

    @Builder
    public AttachmentToResource(byte[] resource, String encodedFileName, String extension, String contentType) {
        this.resource = resource;
        this.encodedFileName = encodedFileName;
        this.extension = extension;
        this.contentType = contentType;
    }

    public static AttachmentToResource from(GetAttachmentResponseDTO dto) {
        try {
            return AttachmentToResource.builder()
                    .resource(Files.readAllBytes(Paths.get(dto.getFilePath())))
                    .encodedFileName(URLEncoder.encode(dto.getFileOriginName(), StandardCharsets.UTF_8))
                    .extension(dto.getFileExtension())
                    .contentType(dto.getContentType())
                    .build();
        } catch (IOException e) {
            log.error("Resource 를 InputStreamResource 로 전환하는 과정 중 에러 발생");
            throw new RuntimeException(e.getMessage());
        }
    }

}
