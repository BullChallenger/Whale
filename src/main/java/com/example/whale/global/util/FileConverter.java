package com.example.whale.global.util;

import com.example.whale.domain.attachment.dto.AttachmentToResource;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {

    public static List<AttachmentToResource> responseFile(List<GetAttachmentResponseDTO> attachments) {
        return attachments.stream().map(AttachmentToResource::from).collect(Collectors.toList());
    }

}
