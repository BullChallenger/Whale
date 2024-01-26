package com.example.whale.service;

import static com.example.whale.constant.FilePath.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.AttachmentEntity;
import com.example.whale.repository.AttachmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

	private String attachmentDir;

	private final AttachmentRepository attachmentRepository;

	@PostConstruct
	private void createStorage() {
		if (new File(ABSOLUTE_PATH.getPath() + "/attachments").mkdirs()) {
			log.info("Created tus upload directory");
		}
	}

	@Transactional
	public void upload(ArticleEntity article, MultipartFile file) {
		Path fileDir = Paths.get(attachmentDir);
		String contentType = file.getContentType();
		String extension = getExtensionFromMultipartFile(file);
		String fileName = String.valueOf(file.getOriginalFilename().split(extension)[0]);

		Path filePath = fileDir.resolve(generateRandomStr() + extension).toAbsolutePath();
		File target = new File(filePath.toString());

		try {
			file.transferTo(target);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장에 실패했습니다.");
		}

		AttachmentEntity attachment = AttachmentEntity.of(
			UUID.randomUUID().toString(),
			article,
			fileName,
			filePath.toString(),
			extension,
			contentType,
			file.getSize()
		);

		attachmentRepository.save(attachment);
	}

	private String generateRandomStr() {
		int leftLimit = 97; // letter 'A'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 12;
		String nanoTime = String.valueOf(System.currentTimeMillis());
		Random random = new Random();

		String randomStr = random.ints(leftLimit, rightLimit + 1)
			.limit(targetStringLength)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();

		return randomStr + "_" + nanoTime;
	}

	private String getExtensionFromMultipartFile (MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		if (originalFileName != null && originalFileName.contains(".")) {
			return originalFileName.substring(originalFileName.lastIndexOf("."));
		}

		return "";
	}

}
