package com.example.whale.domain.order.repository.querydsl;

import static com.example.whale.domain.order.entity.QOrderLineEntity.orderLineEntity;
import static com.example.whale.domain.product.entity.QProductEntity.productEntity;
import static com.example.whale.domain.shop.entity.QShopEntity.shopEntity;

import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.order.model.QOrderLine;
import com.example.whale.domain.product.model.QProduct;
import com.example.whale.domain.shop.model.QShop;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomOrderLineRepository {

    private final JPAQueryFactory queryFactory;

    public List<OrderLine> readOrderLinesInOrder(String orderId) {
        return queryFactory.select(
                new QOrderLine(
                        orderLineEntity.id,
                        new QProduct(
                                productEntity.id,
                                new QShop(
                                        shopEntity.shopId,
                                        shopEntity.shopName,
                                        shopEntity.shopDescription
                                ),
                                productEntity.productName,
                                productEntity.productPrice,
                                productEntity.productStockQty,
                                productEntity.productDescription,
                                productEntity.sellStatus
                        ),
                        orderLineEntity.orderQuantity,
                        orderLineEntity.totalAmount,
                        orderLineEntity.totalAmountBeforeDiscount
                )
        ).from(orderLineEntity)
        .innerJoin(orderLineEntity.product, productEntity)
        .innerJoin(productEntity.provider, shopEntity)
        .where(orderLineEntity.order.id.eq(orderId))
        .fetch();
    }

}
