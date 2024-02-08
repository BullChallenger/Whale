package com.example.whale.domain.product.model;

import lombok.Getter;

@Getter
public class ProductStock {

    private long productStockQty;

    public ProductStock(long productStockQty) {
        this.productStockQty = productStockQty;
    }

    public void replenishProductStockQty(Long quantity) {
        validateOrderStockOutOfBound(quantity);
        this.productStockQty += quantity;
    }

    public void subProductStockQty(Long quantity) {
        validateOrderStockQty(quantity);
        this.productStockQty -= quantity;
    }

    private void validateOrderStockQty(Long quantity) {
        validateOrderStockOutOfBound(quantity);
        if (this.productStockQty < quantity) {
            throw new IllegalArgumentException("해당 주문의 상품 수량보다 재고가 부족합니다.");
        }
    }

    private void validateOrderStockOutOfBound(Long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("잘못된 주문 수량입니다.");
        }
    }

}
