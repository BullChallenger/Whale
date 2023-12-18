package com.example.whale.dto.attachment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import static com.example.whale.constant.FilePath.ABSOLUTE_PATH;

@Getter
public class GetAttachmentResponseDTO implements Serializable {

    private final Long attachmentId;

    private final String fileOriginName;

    private final byte[] fileUrlByteArray;

    @Builder
    @QueryProjection
    public GetAttachmentResponseDTO(Long attachmentId, String fileOriginName, String fileUrl) throws IOException {
        this.attachmentId = attachmentId;
        this.fileOriginName = fileOriginName;
        try {
            InputStream imagePathStream = new FileInputStream(ABSOLUTE_PATH.getPath() + fileUrl);
            this.fileUrlByteArray = IOUtils.toByteArray(imagePathStream);
            imagePathStream.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static GetAttachmentResponseDTO from(Long attachmentId, String fileOriginName, String fileUrl) throws IOException {
        return GetAttachmentResponseDTO.builder()
                                        .attachmentId(attachmentId)
                                        .fileOriginName(fileOriginName)
                                        .fileUrl(fileUrl)
                                        .build();
    }

}