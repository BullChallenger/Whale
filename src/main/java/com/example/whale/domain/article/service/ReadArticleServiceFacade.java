package com.example.whale.domain.article.service;

import com.example.whale.domain.Like.service.LikeService;
import com.example.whale.domain.article.dto.GetArticleResponseConvertDTO;
import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public GetArticleResponseConvertDTO readArticleByIdV2(Long articleId) {
        GetArticleResponseConvertDTO article = articleService.findArticleByIdV2(articleId);
        article.setLikeCount(likeService.getLikeCountInArticle(articleId));
        return article;
    }

    public Page<GetArticlePageResponseDTO> readArticlePage(Long lastArticleId, Pageable pageable) {
        Page<GetArticlePageResponseDTO> articlePages = articleService.readArticlePage(lastArticleId, pageable);
        articlePages.stream().forEach(
                article -> article.setLikeCount(likeService.getLikeCountInArticle(article.getArticleId()))
        );

        return articlePages;
    }

}
