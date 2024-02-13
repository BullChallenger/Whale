package com.example.whale.fixture;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;

import com.example.whale.domain.shop.dto.ShopRegisterRequestDTO;
import com.example.whale.domain.shop.entity.ShopEntity;

public class ShopFixture {

    public static ShopEntity returnShopEntityValue() {
        ShopEntity fixture = Mockito.spy(ShopEntity.of(
            "testShop",
            "this shop is dummy"
        ));

        when(fixture.getShopId()).thenReturn(1L);
        return fixture;
    }

    public static ShopRegisterRequestDTO returnRegisterShopRequestDTO() {
        return new ShopRegisterRequestDTO(
                "dummy Shop",
                "This shop is dummy"
        );
    }

}
