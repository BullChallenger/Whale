package com.example.whale.fixture;

import java.util.List;

import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.user.model.Customer;

public class OrderFixture {

    public static CreatePurchaseOrderRequestDTO returnPurchaseOrderDTO(String productId) {
        return new CreatePurchaseOrderRequestDTO(
                1L,
                List.of(new PurchaseOrderLineRequestDTO(
                        productId,
                        1L
                ))
        );
    }

    public static OrderLineEntity returnOrderLineEntityValue(ProductEntity product) {
        OrderLineEntity orderLine = OrderLineEntity.of(
            "1",
            product,
            3L
        );

        orderLine.insertInOrder(returnOrderEntityValue());
        return orderLine;
    }

    public static OrderLine returnOrderLineValue(Product product) {
        return OrderLine.of(
            "1",
            product,
            3L
        );
    }

    public static Order returnOrderValue() {
        return Order.fromEntity(returnOrderEntityValue());
    }

    public static OrderEntity returnOrderEntityValue() {
        return OrderEntity.of(
            Customer.fromEntity(UserFixture.getDefaultUserEntityFixture())
        );
    }

}
