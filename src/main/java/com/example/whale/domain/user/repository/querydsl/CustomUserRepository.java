package com.example.whale.domain.user.repository.querydsl;

import static com.example.whale.domain.user.entity.QUserEntity.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.whale.domain.user.model.Customer;
import com.example.whale.domain.user.model.QCustomer;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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

    public Optional<Customer> findCustomerById(Long userId) {
        return Optional.ofNullable(queryFactory
            .select(
                new QCustomer(
                    userEntity.id,
                    userEntity.email,
                    userEntity.username,
                    userEntity.nickname
                )
            ).from(userEntity)
            .where(userEntity.id.eq(userId))
            .fetchFirst()
        );

    }

}
