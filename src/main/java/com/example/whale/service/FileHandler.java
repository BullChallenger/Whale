package com.example.whale.service;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.AttachmentEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.whale.constant.FilePath.ABSOLUTE_PATH;

@Component
public class FileHandler {

    private static final String IMAGE_PATH = "images";
    private static final String JPG_EXTENSION = ".jpg";
    private static final String PNG_EXTENSION = ".png";

    public List<AttachmentEntity> parseFileInfo(ArticleEntity article, List<MultipartFile> files) throws IOException {
        List<AttachmentEntity> attachments = new ArrayList<>();

        if (!CollectionUtils.isEmpty(files)) {
            String uploadDate = uploadDateFormat();
            // 해당 Project 내부에 저장하기 위한 절대경로 설정
            String absolutePath = ABSOLUTE_PATH.getPath();
            // 세부 경로 설정
            String detailPath = IMAGE_PATH + File.separator + uploadDate;
            File fileDir = new File(detailPath);
            createImageDirIfNotExists(fileDir);

            for (MultipartFile file : files) {
                String contentType = file.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                }

                String originFileExtension = validateExtension(contentType);
                if (originFileExtension == null) break;

                String fileNameInStorage = System.nanoTime() + originFileExtension;
                AttachmentEntity attachment = AttachmentEntity.of(
                        article,
                        fileNameInStorage,
                        file.getOriginalFilename(),
                detailPath + File.separator + fileNameInStorage,
                        file.getSize()
                );

                attachments.add(attachment);
                fileDir = new File(absolutePath + detailPath + File.separator + fileNameInStorage);

                file.transferTo(fileDir);
                fileDir.setWritable(true);
                fileDir.setReadable(true);
            }
        }

        return attachments;
    }

    private static void createImageDirIfNotExists(File fileDir) throws FileNotFoundException {
        if (!fileDir.exists()) {
            boolean isSuccessMkDir = fileDir.mkdirs();
            if (!isSuccessMkDir) {
                throw new FileNotFoundException("파일 생성에 실패했습니다!");
            }
        }
    }

    private static String validateExtension(String contentType) {
        String originFileExtension;
        if (contentType.contains(MediaType.IMAGE_JPEG_VALUE)) {
            originFileExtension = JPG_EXTENSION;
        } else if (contentType.contains(MediaType.IMAGE_PNG_VALUE)) {
            originFileExtension = PNG_EXTENSION;
        } else {
            return null;
        }

        return originFileExtension;
    }

    private String uploadDateFormat() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter);
    }

}
