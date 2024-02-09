package com.example.whale.domain.product.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.shop.model.Shop;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

    private final String productId;
    private Shop provider;
    private String productName;
    private ProductPrice productPrice;
    private ProductStock productStockQty;
    private String productDescription;
    private SellStatus sellStatus;

    @Builder
    @QueryProjection
    public Product(String productId, Shop provider, String productName, BigDecimal productPrice, Long productStockQty,
                   String productDescription, SellStatus sellStatus) {
        this.productId = productId;
        this.provider = provider;
        this.productName = productName;
        this.productPrice = new ProductPrice(productPrice);
        this.productStockQty = new ProductStock(productStockQty);
        this.productDescription = productDescription;
        this.sellStatus = sellStatus;
    }

    public static Product fromEntity(ProductEntity entity) {
        return Product.builder()
                .productId(entity.getId())
                .provider(Shop.fromEntity(entity.getProvider()))
                .productName(entity.getProductName())
                .productPrice(entity.getProductPrice())
                .productStockQty(entity.getProductStockQty())
                .productDescription(entity.getProductDescription())
                .sellStatus(entity.getSellStatus())
                .build();
    }

    // TODO: DTO 확정 시 파라미터 변경
    public static Product of(
        Shop provider,
        String productName,
        BigDecimal productPrice,
        Long productStockQty,
        String productDescription,
        SellStatus sellStatus
    ) {
        return Product.builder()
            .productId(generateProductId())
            .provider(provider)
            .productName(productName)
            .productPrice(productPrice)
            .productStockQty(productStockQty)
            .productDescription(productDescription)
            .sellStatus(sellStatus)
            .build();
    }

    private static String generateProductId() {
        return UUID.randomUUID().toString();
    }

}
