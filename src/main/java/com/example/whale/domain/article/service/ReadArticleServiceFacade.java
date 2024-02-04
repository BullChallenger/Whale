package com.example.whale.domain.article.service;

import com.example.whale.domain.Heart.service.LikeService;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
