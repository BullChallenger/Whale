package com.example.whale.domain.Like.repository.querydsl;

import static com.example.whale.domain.Like.entity.QHeartEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomLikeRepository {

    private final JPAQueryFactory queryFactory;

    public boolean isLikeExists(Long userId, Long articleId) {
        return queryFactory
                .selectOne()
                .from(heartEntity)
                .where(heartEntity.user.id.eq(userId).and(heartEntity.article.id.eq(articleId)))
                .fetchFirst() != null;
    }

}
