package com.example.whale.domain.product.model;

import lombok.Getter;

@Getter
public class ProductStock {

    private long productStockQty;

    public ProductStock(long productStockQty) {
        this.productStockQty = productStockQty;
    }

    public void replenishProductStockQty(Long quantity) {
        this.productStockQty += quantity;
    }

    public void subProductStockQty(Long quantity) {
        this.productStockQty -= quantity;
    }

}
