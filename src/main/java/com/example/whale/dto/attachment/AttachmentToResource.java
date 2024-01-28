package com.example.whale.dto.attachment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Getter
public class AttachmentToResource {

    private final Resource resource;
    private final String encodedFileName;
    private final String extension;
    private final String contentType;

    @Builder
    public AttachmentToResource(Resource resource, String encodedFileName, String extension, String contentType) {
        this.resource = resource;
        this.encodedFileName = encodedFileName;
        this.extension = extension;
        this.contentType = contentType;
    }

    public static AttachmentToResource from(GetAttachmentResponseDTO dto) {
        return AttachmentToResource.builder()
                .resource(new FileSystemResource(Paths.get(dto.getFilePath())))
                .encodedFileName(URLEncoder.encode(dto.getFileOriginName(), StandardCharsets.UTF_8))
                .extension(dto.getFileExtension())
                .contentType(dto.getContentType())
                .build();
    }

}
