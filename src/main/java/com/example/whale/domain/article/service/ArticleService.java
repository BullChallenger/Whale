package com.example.whale.domain.article.service;

import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.attachment.entity.AttachmentEntity;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.article.dto.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.user.repository.UserRepository;
import com.example.whale.domain.article.repository.querydsl.CustomArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomArticleRepository customArticleRepository;

    public ArticleEntity saveArticle(Long userId, CreateArticleRequestDTO dto, List<MultipartFile> attachments) throws IOException {
        UserEntity writer = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );

        ArticleEntity article = ArticleEntity.of(
                writer,
                dto.getTitle(),
                dto.getContent()
        );

        return articleRepository.save(article);
    }

    public GetArticleResponseDTO findArticleById(Long articleId) {
        return customArticleRepository.readArticleById(articleId);
    }

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

    public void deleteArticleById(Long articleId) {
        customArticleRepository.deleteArticleById(articleId);
    }

    public Page<GetArticlePageResponseDTO> readArticlePage(Long lastArticleId, Pageable pageable) {
        return customArticleRepository.readArticlePage(lastArticleId, pageable);
    }

}