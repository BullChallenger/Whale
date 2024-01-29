package com.example.whale.dto.attachment;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

@Slf4j
@Getter
public class AttachmentToResource implements Serializable {

    private final Resource resource;
    private final String encodedFileName;
    private final String extension;
    private final String contentType;
    private final Long size;

    @Builder
    public AttachmentToResource(Resource resource, String encodedFileName, String extension, String contentType,
                                Long size) {
        this.resource = resource;
        this.encodedFileName = encodedFileName;
        this.extension = extension;
        this.contentType = contentType;
        this.size = size;
    }

    public static AttachmentToResource from(GetAttachmentResponseDTO dto) {
        return AttachmentToResource.builder()
                .resource(new FileSystemResource(Paths.get(dto.getFilePath())))
                .encodedFileName(URLEncoder.encode(dto.getFileOriginName(), StandardCharsets.UTF_8))
                .extension(dto.getFileExtension())
                .contentType(dto.getContentType())
                .size(dto.getSize())
                .build();
    }

}
