package com.example.whale.repository.querydsl;

import static com.example.whale.domain.QArticleEntity.articleEntity;
import static com.example.whale.domain.QUserEntity.userEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public boolean isUserExists(Long userId) {
        return queryFactory.from(userEntity).where(userEntity.id.eq(userId)).fetchOne() != null;
    }

}
