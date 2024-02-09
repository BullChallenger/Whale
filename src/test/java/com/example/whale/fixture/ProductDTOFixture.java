package com.example.whale.fixture;

import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import java.math.BigDecimal;

public class ProductDTOFixture {

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

}
