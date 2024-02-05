package com.example.whale.domain.Like.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.example.whale.domain.Like.dto.AddLikeRequestDTO;
import com.example.whale.domain.Like.entity.LikeEntity;
import com.example.whale.domain.Like.repository.HeartRepository;
import com.example.whale.domain.Like.repository.querydsl.CustomLikeRepository;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final HeartRepository heartRepository;
    private final CustomLikeRepository customHeartRepository;

    public void addHeart(AddLikeRequestDTO dto) {
        Long userId = dto.getUserId();
        Long articleId = dto.getArticleId();

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 유저")
        );
        ArticleEntity article = articleRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 게시글")
        );

        if (customHeartRepository.isLikeExists(userId, articleId)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        heartRepository.save(LikeEntity.of(user, article));
    }

}
