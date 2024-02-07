package com.example.whale.domain.product.entity;

import java.math.BigDecimal;

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

import com.example.whale.domain.common.entity.BaseEntity;
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
public class ProductEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String productId;

    @ManyToOne
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
        this.productId = productId;
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

    public void replenishProductStockQty(Long quantity) {
        this.productStockQty += quantity;
    }

    public void subProductStockQty(Long quantity) {
        this.productStockQty -= quantity;
    }

    public void updateProductName(String productName) {
        this.productName = productName;
    }

    public void updateProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

}
