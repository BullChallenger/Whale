package com.example.whale.util;

import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.attachment.AttachmentToResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultipartResponseUtil {

    private final ObjectMapper objectMapper;

    private static final String BOUNDARY = "whale_boundary";
    private static final String NEW_LINE = "\r\n";

    // Multipart 응답 생성 메서드
    public byte[] createMultipartResponse(GetArticleResponseDTO dto, List<AttachmentToResource> resources) {
        StringBuilder responseBuilder = new StringBuilder();

        try {
            String jsonString = objectMapper.writeValueAsString(dto);
            responseBuilder.append("--").append(BOUNDARY).append(NEW_LINE);
            responseBuilder.append("Content-Type: application/json").append(NEW_LINE);
            responseBuilder.append("Content-Length: ").append(StringUtils.getBytesUtf8(jsonString).length).append(NEW_LINE);
            responseBuilder.append(NEW_LINE);
            responseBuilder.append(jsonString).append(NEW_LINE);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        for (AttachmentToResource resource : resources) {
            try {
                appendFilePart(responseBuilder, resource);
            } catch (IOException e) {
                log.error("Resource 의 길이를 가져오는 곳에서 에러 발생");
                throw new RuntimeException(e);
            }
        }
        responseBuilder.append("--").append(BOUNDARY).append("--").append(NEW_LINE);
        return responseBuilder.toString().getBytes();
    }

    // 파일 부분 추가 메서드
    private void appendFilePart(StringBuilder builder, AttachmentToResource resource) throws IOException {
        builder.append("--").append(BOUNDARY).append(NEW_LINE);
        builder.append("Content-Disposition: ").append(buildContentDispositionHeaderValue(
                resource.getEncodedFileName(), resource.getExtension())).append(NEW_LINE);
        builder.append("Content-Type: ").append(resource.getContentType() + ";charset=UTF-8").append(NEW_LINE);
        builder.append("Content-Length: ").append(resource.getResource().contentLength()).append(NEW_LINE);
        builder.append(NEW_LINE);
        builder.append(resource.getResource()).append(NEW_LINE);
    }

    private boolean isImage(String ext) {
        return ".png".equalsIgnoreCase(ext) || ".jpg".equalsIgnoreCase(ext);
    }

    private String buildContentDispositionHeaderValue(String encodedFileName, String ext) {
        if (isImage(ext)) {
            return "inline; filename=\"" + encodedFileName + ext + "\"";
        } else {
            return "attachment; filename=\"" + encodedFileName + ext + "\"";
        }
    }


}
