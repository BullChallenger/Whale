package com.example.whale.domain.shop.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.shop.dto.ConfirmOrderDTO;
import com.example.whale.fixture.OrderFixture;
import com.example.whale.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

	@Mock
	private OrderLineRepository orderLineRepository;

	@InjectMocks
	private ShopService shopService;

	private ConfirmOrderDTO returnConfirmOrderDTOValue() {
		return new ConfirmOrderDTO(1L, "1", OrderStatus.WAITING_DELIVERY);
	}

	@Test
	@Disabled
	@DisplayName(value = "[성공]_판매자가_주문을_확인했을_때_주문상태_변경")
	void updateOrderStatusWhenShopConfirmedOrder() {
		// given
		Mockito.doReturn(Optional.of(OrderFixture.returnOrderLineEntityValue(ProductFixture.returnProductEntityValue())))
			.when(orderLineRepository).findById(anyString());

		// when
		OrderLine orderLine = shopService.confirmOrder(returnConfirmOrderDTOValue());

		// then
		assertThat(orderLine.getOrderStatus().getStatus()).isEqualTo(OrderStatus.WAITING_DELIVERY.getStatus());
	}

}