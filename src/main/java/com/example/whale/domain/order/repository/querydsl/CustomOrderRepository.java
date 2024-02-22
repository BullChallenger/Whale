package com.example.whale.domain.order.repository.querydsl;

import static com.example.whale.domain.order.entity.QOrderEntity.*;
import static com.example.whale.domain.user.entity.QUserEntity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.example.whale.domain.delivery.model.QAddress;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.model.QOrder;
import com.example.whale.domain.user.model.QCustomer;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Order> readOrdersByCustomerId(Long customerId, Pageable pageable) {
        List<Order> pageContent = queryFactory.select(
                        new QOrder(
                                orderEntity.id,
                                orderEntity.customerId,
                                orderEntity.totalAmountOfOrder,
                                new QAddress(
                                    orderEntity.destination.id,
                                    new QCustomer(
                                        userEntity.id,
                                        userEntity.email,
                                        userEntity.username,
                                        userEntity.nickname
                                    ),
                                    orderEntity.destination.zipcode,
                                    orderEntity.destination.address,
                                    orderEntity.destination.detailAddress
                                )
                        )
                ).from(orderEntity)
                .where(orderEntity.customerId.eq(customerId))
                .innerJoin(userEntity).on(orderEntity.customerId.eq(userEntity.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countOrderQuery =
                queryFactory
                        .select(orderEntity.count())
                        .from(orderEntity)
                        .where(orderEntity.customerId.eq(customerId));

        return PageableExecutionUtils.getPage(pageContent, pageable, countOrderQuery::fetchOne);
    }

}
