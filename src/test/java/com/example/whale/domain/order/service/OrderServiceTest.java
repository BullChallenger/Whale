package com.example.whale.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.order.repository.OrderRepository;
import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.model.Customer;
import com.example.whale.domain.user.repository.querydsl.CustomUserRepository;
import com.example.whale.fixture.UserFixture;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private static final String DUMMY_PRODUCT_ID = "IIIIIIIIIII";

	@Mock
	private CustomUserRepository customUserRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderLineRepository orderLineRepository;

	@InjectMocks
	private OrderService orderService;

	private UserEntity returnUserEntityValue() {
		UserEntity fixture = Mockito.spy(UserFixture.getDefaultUserEntityFixture());
		when(fixture.getId()).thenReturn(1L);

		return fixture;
	}

	private CreatePurchaseOrderRequestDTO returnPurchaseOrderDTO() {
		return new CreatePurchaseOrderRequestDTO(
			1L,
			List.of(new PurchaseOrderLineRequestDTO(DUMMY_PRODUCT_ID, 3L))
		);
	}

	private ShopEntity returnShopEntityValue() {
		return ShopEntity.of(
			"testShop",
			"this shop is dummy"
		);
	}

	private OrderEntity returnOrderEntityValue() {
		return OrderEntity.builder()
			.id("1:" + System.currentTimeMillis())
			.customerId(1L)
			.build();
	}

	private ProductEntity returnProductEntityValue() {
		return ProductEntity.builder()
			.productId(DUMMY_PRODUCT_ID)
			.provider(returnShopEntityValue())
			.productName("Dummy Product")
			.productStockQty(10L)
			.productPrice(BigDecimal.valueOf(1000L))
			.productDescription("This product is dummy")
			.sellStatus(SellStatus.WAITING_FOR_SALE)
			.build();
	}

	@Test
	@DisplayName(value = "[성공]_주문_생성")
	void createOrderSuccessTest() {
		// given
		Mockito.doReturn(List.of(returnProductEntityValue())).when(productRepository).findProductsByIds(anyList());
		Mockito.doReturn(Optional.ofNullable(Customer.fromEntity(returnUserEntityValue()))).when(customUserRepository).findCustomerById(1L);
		Mockito.doReturn(returnOrderEntityValue()).when(orderRepository).save(any(OrderEntity.class));
		Mockito.doReturn(List.of(returnOrderEntityValue())).when(orderLineRepository).saveAll(anyList());

		// when
		Order purchaseOrder = orderService.createPurchaseOrder(returnPurchaseOrderDTO());

		// then
		assertThat(purchaseOrder.getTotalAmountOfOrder()).isEqualTo(BigDecimal.valueOf(3000L));
	}

}