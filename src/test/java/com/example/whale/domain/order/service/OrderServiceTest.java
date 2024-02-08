package com.example.whale.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.order.repository.OrderRepository;
import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.repository.querydsl.CustomProductRepository;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.model.Customer;
import com.example.whale.domain.user.repository.querydsl.CustomUserRepository;
import com.example.whale.global.constant.Role;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private static final String DUMMY_PRODUCT_ID = "IIIIIIIIIII";

	@Mock
	private CustomUserRepository customUserRepository;
	@Mock
	private CustomProductRepository customProductRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderLineRepository orderLineRepository;

	@InjectMocks
	private OrderService orderService;

	private UserEntity returnUserEntityValue() {
		return UserEntity.of(
			"dummy@dummy.com",
			"john doe",
			"dummy",
			"{noop}1234",
			Role.USER
		);
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

	private OrderLineEntity returnOrderLineEntityValue() {
		OrderEntity orderEntity = returnOrderEntityValue();
		ProductEntity productEntity = returnProductEntityValue();
		long quantity = 3L;

		return OrderLineEntity.builder()
			.orderLineId(orderEntity.getOrderId())
			.product(productEntity)
			.order(orderEntity)
			.orderQuantity(quantity)
			.totalAmount(productEntity.getProductPrice().multiply(BigDecimal.valueOf(quantity)))
			.build();
	}

	private OrderEntity returnOrderEntityValue() {
		List<OrderLineEntity> orderLines = List.of(returnOrderLineEntityValue());
		UserEntity userEntity = returnUserEntityValue();
		return OrderEntity.builder()
			.orderId(userEntity.getId().toString() + LocalDateTime.now())
			.customerId(userEntity.getId())
			.orderLines(orderLines)
			.orderStatus(OrderStatus.NEW_ORDER)
			.totalAmountOfOrder(orderLines.stream().map(OrderLineEntity::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
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
		Mockito.doReturn(List.of(returnProductEntityValue())).when(customProductRepository).readProductDetailsByIds(anyList());
		Mockito.doReturn(Optional.ofNullable(Customer.fromEntity(returnUserEntityValue()))).when(customUserRepository).findCustomerById(anyLong());
		Mockito.doReturn(returnOrderEntityValue()).when(orderRepository).save(any(OrderEntity.class));
		Mockito.doReturn(returnOrderLineEntityValue()).when(orderLineRepository).save(any(OrderLineEntity.class));

		// when
		Order purchaseOrder = orderService.createPurchaseOrder(returnPurchaseOrderDTO());

		// then
		assertThat(purchaseOrder.getOrderLines().get(0)).isEqualTo(returnOrderLineEntityValue());
	}

}