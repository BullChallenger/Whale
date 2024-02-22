package com.example.whale.domain.article.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.article.dto.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.GetArticleResponseConvertDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.article.repository.querydsl.CustomArticleRepository;
import com.example.whale.domain.attachment.entity.AttachmentEntity;
import com.example.whale.domain.attachment.service.UploadService;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomArticleRepository customArticleRepository;
    private final UploadService uploadService;

    @Transactional
    public ArticleEntity saveArticle(Long userId, CreateArticleRequestDTO dto, List<MultipartFile> attachments) throws IOException {
        UserEntity writer = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );

        ArticleEntity article = ArticleEntity.of(
                writer,
                dto.getTitle(),
                dto.getContent()
        );

        entityManager.persist(article);

        if (attachments != null) {
            article.getAttachments().addAttachmentsInArticle(uploadService.uploadAttachmentInArticle(article, attachments));
        }

        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public GetArticleResponseConvertDTO findArticleByIdV2(Long articleId) {
        return customArticleRepository.readArticleByIdV2(articleId);
    }

    @Transactional(readOnly = true)
    public GetArticleResponseDTO findArticleById(Long articleId) {
        return customArticleRepository.readArticleById(articleId);
    }

    @Transactional
    public UpdateArticleResponseDTO updateArticle(UpdateArticleRequestDTO dto, List<MultipartFile> attachments) throws IOException {
        ArticleEntity findArticle = articleRepository.findById(dto.getArticleId()).orElseThrow(
                () -> new EntityNotFoundException("Article Not Found!")
        );

        if (dto.getTitle() != null) {
            findArticle.updateTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            findArticle.updateContent(dto.getContent());
        }

        List<AttachmentEntity> attachmentsInArticle = findArticle.getAttachments().attachments();
        attachmentsInArticle.clear();

        if (attachments != null) {
            attachmentsInArticle.addAll(uploadService.uploadAttachmentInArticle(findArticle, attachments));
        }

        return UpdateArticleResponseDTO.from(findArticle);
    }

    @Transactional
    public void deleteArticleById(Long articleId) {
        customArticleRepository.deleteArticleById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<GetArticlePageResponseDTO> readArticlePage(Long lastArticleId, Pageable pageable) {
        return customArticleRepository.readArticlePage(lastArticleId, pageable);
    }

}