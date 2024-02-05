package com.example.whale.domain.product.model;

import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.entity.ProductEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

    private final String productId;
    private String productName;
    private ProductPrice productPrice;
    private ProductStock productStock;
    private String productDescription;
    private SellStatus sellStatus;

    @Builder
    public Product(String productId, String productName, ProductPrice productPrice, ProductStock productStock,
                   String productDescription, SellStatus sellStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.sellStatus = sellStatus;
    }

    public static Product fromEntity(ProductEntity entity) {
        return Product.builder()
                .productId(entity.getProductId())
                .productName()
                .productPrice()
                .productStock()
                .productDescription()
                .sellStatus()
                .build();
    }

}
