package com.example.whale.domain.Heart.service;

import com.example.whale.domain.Heart.dto.AddLikeRequestDTO;
import com.example.whale.domain.Heart.dto.SubLikeRequestDTO;
import com.example.whale.domain.Heart.repository.CacheLikeRepository;
import com.example.whale.domain.article.repository.querydsl.CustomArticleRepository;
import com.example.whale.domain.user.repository.querydsl.CustomUserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CustomUserRepository customUserRepository;
    private final CustomArticleRepository customArticleRepository;
    private final CacheLikeRepository cacheLikeRepository;

    public void addLikeInArticle(AddLikeRequestDTO dto) {
        Long articleId = dto.getArticleId();
        Long userId = dto.getUserId();
        if (!customArticleRepository.isArticleExists(articleId)) {
            throw new EntityNotFoundException("해당 게시글은 존재하지 않습니다.");
        }
        if (!customUserRepository.isUserExists(userId)) {
            throw new EntityNotFoundException("해당 유저는 존재하지 않습니다.");
        }

        if (cacheLikeRepository.isAlreadyLiked(articleId, userId)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글 입니다.");
        }

        cacheLikeRepository.addArticleLike(articleId, userId);
    }

    public void subLikeInArticle(SubLikeRequestDTO dto) {
        Long articleId = dto.getArticleId();
        Long userId = dto.getUserId();

        if (!customArticleRepository.isArticleExists(articleId)) {
            throw new EntityNotFoundException("해당 게시글은 존재하지 않습니다.");
        }
        if (!customUserRepository.isUserExists(userId)) {
            throw new EntityNotFoundException("해당 유저는 존재하지 않습니다.");
        }

        if (!cacheLikeRepository.isAlreadyLiked(articleId, userId)) {
            throw new IllegalArgumentException("이미 좋아요를 취소한 게시글 입니다.");
        }

        cacheLikeRepository.subArticleLike(articleId, userId);
    }

    public Long getLikeCountInArticle(Long articleId) {
        return cacheLikeRepository.countLikesInArticle(articleId);
    }

}
