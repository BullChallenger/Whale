package com.example.whale.domain.order.repository.querydsl;

import static com.example.whale.domain.order.entity.QOrderEntity.orderEntity;

import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.model.QOrder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Order> readOrdersByCustomerId(Long customerId, Pageable pageable) {
        List<Order> pageContent = queryFactory.select(
                        new QOrder(
                                orderEntity.id,
                                orderEntity.customerId,
                                orderEntity.orderStatus,
                                orderEntity.totalAmountOfOrder
                        )
                ).from(orderEntity)
                .where(orderEntity.customerId.eq(customerId))
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