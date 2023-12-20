package com.example.whale.service;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.AttachmentEntity;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.dto.article.GetArticlePageResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.repository.ArticleRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.repository.querydsl.CustomArticleRepository;
import com.example.whale.repository.querydsl.CustomAttachmentRepository;
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

    private final FileHandler fileHandler;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomArticleRepository customArticleRepository;
    private final CustomAttachmentRepository customAttachmentRepository;

    public CreateArticleResponseDTO saveArticle(Long userId, CreateArticleRequestDTO dto, List<MultipartFile> attachments) throws IOException {
        UserEntity writer = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );

        ArticleEntity article = ArticleEntity.of(
                writer,
                dto.getTitle(),
                dto.getContent()
        );

        List<AttachmentEntity> attachmentsInArticle = fileHandler.parseFileInfo(article, attachments);
        article.setAttachmentsInArticle(attachmentsInArticle);
        return CreateArticleResponseDTO.from(articleRepository.save(article));
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
            updatedAttachments = fileHandler.parseFileInfo(findArticle, attachmentsShouldAddArticle);
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
//        Page<ArticleEntity> articleEntityPage = articleRepository.findAll(pageable);
//        return articleEntityPage.map(articleEntity -> GetArticlePageResponseDTO.of(
//                articleEntity.getId(),
//                articleEntity.getTitle(),
//                WriterResponseDTO.of(articleEntity.getWriter().getId(), articleEntity.getWriter().getNickname()),
//                articleEntity.getComments().size()
//                )
//        );

        return customArticleRepository.readArticlePage(lastArticleId, pageable);
    }

}