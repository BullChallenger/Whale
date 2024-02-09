package com.example.whale.fixture;

import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import java.util.List;

public class OrderFixture {

    public static CreatePurchaseOrderRequestDTO returnPurchaseOrderDTO(String productId) {
        return new CreatePurchaseOrderRequestDTO(
                1L,
                List.of(new PurchaseOrderLineRequestDTO(
                        productId,
                        1L
                ))
        );
    }

}
