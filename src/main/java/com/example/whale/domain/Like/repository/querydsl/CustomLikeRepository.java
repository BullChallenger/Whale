package com.example.whale.domain.Like.repository.querydsl;

import static com.example.whale.domain.Like.entity.QLikeEntity.*;

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
                .from(likeEntity)
                .where(likeEntity.user.id.eq(userId).and(likeEntity.article.id.eq(articleId)))
                .fetchFirst() != null;
    }

}
