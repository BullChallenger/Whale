package com.example.whale.service;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.repository.ArticleRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.repository.querydsl.CustomArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomArticleRepository customArticleRepository;

    public CreateArticleResponseDTO saveArticle(CreateArticleRequestDTO dto) {
        UserEntity writer = userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );

        ArticleEntity article = articleRepository.save(ArticleEntity.of(writer, dto.getTitle(), dto.getContent()));
        return CreateArticleResponseDTO.from(article);
    }

    public GetArticleResponseDTO findArticleById(Long articleId) {
        return customArticleRepository.readArticleById(articleId);
    }

    public UpdateArticleResponseDTO updateArticle(UpdateArticleRequestDTO dto) {
        ArticleEntity findArticle = articleRepository.findById(dto.getArticleId()).orElseThrow(
                () -> new EntityNotFoundException("Article Not Found!")
        );

        if (dto.getTitle() != null) {
            findArticle.updateTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            findArticle.updateContent(dto.getContent());
        }

        ArticleEntity updatedArticle = articleRepository.save(findArticle);
        return UpdateArticleResponseDTO.from(updatedArticle);
    }

    public void deleteArticleById(Long articleId) {
        ArticleEntity findArticle = articleRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("Article Not Found!")
        );

        findArticle.setIsDeleted(true);
        articleRepository.save(findArticle);
    }

}