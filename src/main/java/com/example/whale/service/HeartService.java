package com.example.whale.service;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.HeartEntity;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.heart.AddHeartDTO.AddHeartRequestDTO;
import com.example.whale.dto.heart.AddHeartDTO.AddHeartResponseDTO;
import com.example.whale.repository.ArticleRepository;
import com.example.whale.repository.HeartRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.repository.querydsl.CustomHeartRepository;
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

    public AddHeartResponseDTO addHeart(AddHeartRequestDTO dto) {
        UserEntity heartedUser = userRepository.findById(dto.getUserId()).orElseThrow(
                        () -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
        ArticleEntity heartedArticle = articleRepository.findById(dto.getArticleId()).orElseThrow(
                        () -> new EntityNotFoundException("해당 게시글은 존재하지 않습니다."));

        if (customHeartRepository.isHeartExists(heartedUser.getId(), heartedArticle.getId())) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글 입니다.");
        }

        return new AddHeartResponseDTO(heartRepository.save(HeartEntity.of(heartedUser, heartedArticle)));
    }

    public void subHeart(Long heartId) {
        if (!customHeartRepository.isHeartExists(heartId)) {
            throw new IllegalArgumentException("이미 좋아요를 취소한 게시글 입니다.");
        }

        heartRepository.deleteById(heartId);
    }

}
