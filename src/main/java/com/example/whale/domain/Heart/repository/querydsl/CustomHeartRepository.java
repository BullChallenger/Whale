package com.example.whale.domain.Heart.repository.querydsl;

import static com.example.whale.domain.Heart.entity.QHeartEntity.heartEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomHeartRepository {

    private final JPAQueryFactory queryFactory;

    public boolean isHeartExists(Long userId, Long articleId) {
        return queryFactory
                .selectOne()
                .from(heartEntity)
                .where(heartEntity.user.id.eq(userId).and(heartEntity.article.id.eq(articleId)))
                .fetchFirst() != null;
    }

}
