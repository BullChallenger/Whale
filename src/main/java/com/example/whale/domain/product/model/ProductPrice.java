package com.example.whale.domain.product.model;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class ProductPrice {

    private BigDecimal productPrice;

    public ProductPrice(BigDecimal productPrice) {
        if (validatePriceRange(productPrice) <= 0) {
            throw new IllegalArgumentException("0 이하의 가격은 설정될 수 없다.");
        }
        this.productPrice = productPrice;
    }

    private int validatePriceRange(BigDecimal productPrice) {
        return productPrice.compareTo(BigDecimal.ZERO);
    }

}
