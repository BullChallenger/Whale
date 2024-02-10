package com.example.whale.domain.attachment.service;

import com.example.whale.domain.attachment.entity.AttachmentEntity;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.article.entity.ArticleEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {

	private final AttachmentService attachmentService;

	public List<AttachmentEntity> uploadAttachmentInArticle(ArticleEntity article, List<MultipartFile> attachments) {
		List<AttachmentEntity> attachmentsInArticle = new ArrayList<>();
		for (MultipartFile attachment : attachments) {
			attachmentsInArticle.add(attachmentService.upload(article, attachment));
		}

		return attachmentsInArticle;
	}

}
