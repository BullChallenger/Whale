package com.example.whale.domain.article.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.article.dto.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.article.repository.querydsl.CustomArticleRepository;
import com.example.whale.domain.attachment.entity.AttachmentEntity;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomArticleRepository customArticleRepository;

    @Transactional
    public ArticleEntity saveArticle(Long userId, CreateArticleRequestDTO dto, List<MultipartFile> attachments) throws IOException {
        UserEntity writer = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );

        return articleRepository.save(
            ArticleEntity.of(
                writer,
                dto.getTitle(),
                dto.getContent()
            )
        );
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

        List<MultipartFile> attachmentsShouldAddArticle = new ArrayList<>();
        List<AttachmentEntity> updatedAttachments;

        if (CollectionUtils.isEmpty(findArticle.getAttachments())) {
            if (!CollectionUtils.isEmpty(attachments)) {
                attachmentsShouldAddArticle.addAll(attachments);
            }
        } else {
            attachmentsShouldAddArticle.addAll(attachments);
        }

        if (!attachmentsShouldAddArticle.isEmpty()) {
            updatedAttachments = new ArrayList<>();
            findArticle.getAttachments().clear();
            findArticle.getAttachments().addAll(updatedAttachments);
        } else {
            findArticle.getAttachments().clear();
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