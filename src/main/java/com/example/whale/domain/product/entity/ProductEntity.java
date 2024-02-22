package com.example.whale.domain.product.entity;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.PersistableWrapper;
import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.shop.entity.ShopEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends PersistableWrapper {

    @Id
    @Column(nullable = false, unique = true, name = "product_id")
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id")
    private ShopEntity provider;

    private String productName;

    private Long productStockQty;

    private BigDecimal productPrice;

    private String productDescription;

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;

    @Builder
    public ProductEntity(String productId, ShopEntity provider, String productName, Long productStockQty,
        BigDecimal productPrice, String productDescription, SellStatus sellStatus) {
        this.id = productId;
        this.provider = provider;
        this.productName = productName;
        this.productStockQty = productStockQty;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.sellStatus = sellStatus;
    }

    public static ProductEntity of(Product product) {
        return ProductEntity.builder()
            .productId(product.getProductId())
            .provider(ShopEntity.of(product.getProvider()))
            .productName(product.getProductName())
            .productStockQty(product.getProductStockQty().getProductStockQty())
            .productPrice(product.getProductPrice().getProductPrice())
            .productDescription(product.getProductDescription())
            .sellStatus(product.getSellStatus())
            .build();
    }

    public static ProductEntity of(ShopEntity provider, String productName, Long productStockQty, BigDecimal productPrice,
                                   String productDescription, SellStatus sellStatus) {
        return ProductEntity.builder()
                .productId(generateProductId())
                .provider(provider)
                .productName(productName)
                .productStockQty(productStockQty)
                .productPrice(productPrice)
                .productDescription(productDescription)
                .sellStatus(sellStatus)
                .build();
    }

    private static String generateProductId() {
        return UUID.randomUUID().toString();
    }

    public void updateProductName(String productName) {
        this.productName = productName;
    }

    public void updateProductDescription(String productDescription) {
        this.productDescription = productDescription;
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
