package com.example.whale.global.util.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.example.whale.domain.attachment.dto.AttachmentToResource;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;

public class FileConverter {

    public static List<AttachmentToResource> responseFile(List<GetAttachmentResponseDTO> attachments) {
        return attachments.stream().map(AttachmentToResource::from).collect(Collectors.toList());
    }

}
