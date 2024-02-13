package com.example.whale.fixture;

import java.math.BigDecimal;

import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.model.Product;

public class ProductFixture {

    public static Product returnProductValue() {
        return Product.fromEntity(returnProductEntityValue());
    }

    public static ProductRegisterRequestDTO returnProductRegisterDTOValue() {
        return new ProductRegisterRequestDTO(
                1L,
                "Dummy Product",
                BigDecimal.valueOf(1000L),
                10L,
                "This product is dummy",
                SellStatus.WAITING_FOR_SALE
        );
    }

    public static ProductEntity returnProductEntityValue() {
        return ProductEntity.of(
            ShopFixture.returnShopEntityValue(),
            "Dummy",
            100L,
            BigDecimal.valueOf(1000L),
            "Dummy Product",
            SellStatus.SELL
        );
    }
}
