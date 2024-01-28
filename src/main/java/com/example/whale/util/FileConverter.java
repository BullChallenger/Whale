package com.example.whale.util;

import com.example.whale.dto.attachment.AttachmentToResource;
import com.example.whale.dto.attachment.GetAttachmentResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {

    public static List<AttachmentToResource> responseFile(List<GetAttachmentResponseDTO> attachments) {
        return attachments.stream().map(AttachmentToResource::from).collect(Collectors.toList());
    }

}
