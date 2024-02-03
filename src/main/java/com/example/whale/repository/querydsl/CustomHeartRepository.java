package com.example.whale.repository.querydsl;

import static com.example.whale.domain.QHeartEntity.heartEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomHeartRepository {

    private final JPAQueryFactory queryFactory;

    public boolean isHeartExists(Long userId, Long articleId) {
        return queryFactory.from(heartEntity)
                .where(heartEntity.user.id.eq(userId).and(heartEntity.article.id.eq(articleId)))
                .fetchOne() != null;
    }

    public boolean isHeartExists(Long heartId) {
        return queryFactory.from(heartEntity)
                .where(heartEntity.id.eq(heartId))
                .fetchOne() != null;
    }


}
