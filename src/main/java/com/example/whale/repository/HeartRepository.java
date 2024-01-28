package com.example.whale.repository;

import com.example.whale.domain.HeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
}
