package com.example.whale.domain.Heart.service;

import com.example.whale.domain.Heart.dto.AddLikeRequestDTO;
import com.example.whale.domain.Heart.entity.HeartEntity;
import com.example.whale.domain.Heart.repository.HeartRepository;
import com.example.whale.domain.Heart.repository.querydsl.CustomHeartRepository;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final HeartRepository heartRepository;
    private final CustomHeartRepository customHeartRepository;

    public void addHeart(AddLikeRequestDTO dto) {
        Long userId = dto.getUserId();
        Long articleId = dto.getArticleId();

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 유저")
        );
        ArticleEntity article = articleRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 게시글")
        );

        if (customHeartRepository.isHeartExists(userId, articleId)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        heartRepository.save(HeartEntity.of(user, article));
    }

}
