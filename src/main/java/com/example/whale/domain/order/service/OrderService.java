package com.example.whale.domain.order.service;

import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.order.repository.OrderRepository;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.product.repository.querydsl.CustomProductRepository;
import com.example.whale.domain.user.model.Customer;
import com.example.whale.domain.user.repository.querydsl.CustomUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final CustomUserRepository customUserRepository;
	private final ProductRepository productRepository;
	private final CustomProductRepository customProductRepository;
	private final OrderRepository orderRepository;
	private final OrderLineRepository orderLineRepository;

	public Order createPurchaseOrder(CreatePurchaseOrderRequestDTO dto) {
		Customer customer = customUserRepository.findCustomerById(dto.getUserId()).orElseThrow(
			() -> new EntityNotFoundException("해당 고객을 찾을 수 없습니다.")
		);

		List<String> productIds = dto.getOrderLines().stream().map(PurchaseOrderLineRequestDTO::getProductId).toList();
		List<Long> orderQuantities = dto.getOrderLines().stream().map(PurchaseOrderLineRequestDTO::getQuantity).toList();
		List<Product> products = customProductRepository.readProductDetailsByIds(productIds);

		Order order = Order.of(customer);

		List<OrderLine> orderLines = IntStream.range(0, products.size())
			.mapToObj(idx -> OrderLine.of(
				generateOrderLineId(order.getOrderId(), products.get(idx).getProductId()),
				products.get(idx),
				orderQuantities.get(idx)
			)
		).toList();

		order.insertOrderLines(orderLines);
		order.updateOrderStatus(OrderStatus.NEW_ORDER);
		if (!order.isOrderLineNotEmpty()) {
			throw new IllegalArgumentException("잘못된 주문 내역입니다.");
		}
		order.calculateTotalAmountOfOrder();

		orderLines.forEach(orderLine -> orderLine.insertInOrder(order));
		orderLineRepository.saveAll(OrderLineEntity.collectToListOf(orderLines));
		orderRepository.save(OrderEntity.of(order));

		return order;
	}

	private String generateOrderLineId(String orderId, String productId) {
		return orderId + ":" + productId;
	}

}
