package com.example.whale.domain.Heart.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CacheLikeRepository {

    private final RedisTemplate<String, Long> redisTemplateForLike;

    public void addArticleLike(Long articleId, Long userId) {
        redisTemplateForLike.opsForSet().add(getKey(articleId), userId);
    }

    public void subArticleLike(Long articleId, Long userId) {
        redisTemplateForLike.opsForSet().remove(getKey(articleId), userId);
    }

    public Long countLikesInArticle(Long articleId) {
        return redisTemplateForLike.opsForSet().size(getKey(articleId));
    }

    public Boolean isAlreadyLiked(Long article, Long userId) {
        return redisTemplateForLike.opsForSet().isMember(getKey(article), userId);
    }

    private String getKey(Long articleId) {
        return "article_like:" + articleId;
    }

}
