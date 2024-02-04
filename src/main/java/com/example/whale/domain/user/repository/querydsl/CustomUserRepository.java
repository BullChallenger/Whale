package com.example.whale.domain.user.repository.querydsl;

import static com.example.whale.domain.user.entity.QUserEntity.userEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public boolean isUserExists(Long userId) {
        return queryFactory
                .selectOne()
                .from(userEntity)
                .where(userEntity.id.eq(userId))
                .fetchFirst() != null;
    }

}
