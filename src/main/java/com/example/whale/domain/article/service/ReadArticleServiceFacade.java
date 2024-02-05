package com.example.whale.domain.article.service;

import org.springframework.stereotype.Service;

import com.example.whale.domain.Like.service.LikeService;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadArticleServiceFacade {

    private final ArticleService articleService;
    private final LikeService likeService;

    public GetArticleResponseDTO readArticleById(Long articleId) {
        GetArticleResponseDTO article = articleService.findArticleById(articleId);
        article.setLikeCount(likeService.getLikeCountInArticle(articleId));
        return article;
    }

}
