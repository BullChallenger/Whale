package com.example.whale.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.ArticleEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {

	private final AttachmentService attachmentService;

	public void uploadAttachmentInArticle(ArticleEntity article, List<MultipartFile> attachments) {
		for (MultipartFile attachment : attachments) {
			attachmentService.upload(article, attachment);
		}
	}

}
