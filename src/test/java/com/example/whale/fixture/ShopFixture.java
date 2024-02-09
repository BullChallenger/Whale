package com.example.whale.fixture;

import com.example.whale.domain.shop.dto.ShopRegisterRequestDTO;
import com.example.whale.domain.shop.entity.ShopEntity;

public class ShopFixture {

    public static ShopEntity returnShopEntityValue() {
        return ShopEntity.of(
                "testShop",
                "this shop is dummy"
        );
    }

    public static ShopRegisterRequestDTO returnRegisterShopRequestDTO() {
        return new ShopRegisterRequestDTO(
                "dummy Shop",
                "This shop is dummy"
        );
    }

}
