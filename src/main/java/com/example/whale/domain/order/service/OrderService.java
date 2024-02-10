package com.example.whale.domain.order.service;

import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.order.repository.querydsl.CustomOrderLineRepository;
import com.example.whale.domain.order.repository.querydsl.CustomOrderRepository;
import com.example.whale.domain.product.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.PurchaseOrderLineRequestDTO;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.order.repository.OrderRepository;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.user.model.Customer;
import com.example.whale.domain.user.repository.querydsl.CustomUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final CustomUserRepository customUserRepository;
	private final ProductRepository productRepository;
	private final CustomOrderRepository customOrderRepository;
	private final OrderRepository orderRepository;
	private final CustomOrderLineRepository customOrderLineRepository;
	private final OrderLineRepository orderLineRepository;

	@Transactional
	public Order createPurchaseOrder(CreatePurchaseOrderRequestDTO dto) {
		Customer customer = customUserRepository.findCustomerById(dto.getUserId()).orElseThrow(
			() -> new EntityNotFoundException("해당 고객을 찾을 수 없습니다.")
		);

		List<String> productIds = dto.getOrderLines().stream().map(PurchaseOrderLineRequestDTO::getProductId).toList();
		List<Long> orderQuantities = dto.getOrderLines().stream().map(PurchaseOrderLineRequestDTO::getQuantity).toList();
		List<ProductEntity> products = productRepository.findProductsByIds(productIds);

		OrderEntity order = OrderEntity.of(customer);
		List<OrderLineEntity> orderLines = combineProductInsideOrderLine(orderQuantities, products, order);

		order.getOrderLineCollection().addOrderLineInOrder(order, orderLines);
		order.calculateTotalAmountOfOrder();
		order.updateOrderStatus(OrderStatus.NEW_ORDER);

		orderRepository.save(order);
		orderLineRepository.saveAll(orderLines);
		productRepository.saveAll(products);

		return Order.fromEntity(order);
	}

	private List<OrderLineEntity> combineProductInsideOrderLine(List<Long> orderQuantities, List<ProductEntity> products,
													   OrderEntity order) {
		List<OrderLineEntity> orderLines = new ArrayList<>();

		for (int idx = 0; idx < products.size(); idx++) {
			ProductEntity target = products.get(idx);
			Long orderQuantity = orderQuantities.get(idx);
			target.subProductStockQty(orderQuantity);
			orderLines.add(
					OrderLineEntity.of(
							generateOrderLineId(order.getId(), target.getId()),
							target,
							orderQuantity
					)
			);
		}

		return orderLines;
	}

	private String generateOrderLineId(String orderId, String productId) {
		return orderId + ":" + productId;
	}

	@Transactional(readOnly = true)
	public Page<Order> readOrdersByCustomerId(Long customerId, Pageable pageable) {
		return customOrderRepository.readOrdersByCustomerId(customerId, pageable);
	}

	@Transactional(readOnly = true)
	public List<OrderLine> readOrderLinesByOrderId(String orderId) {
		return customOrderLineRepository.readOrderLinesInOrder(orderId);
	}

}
