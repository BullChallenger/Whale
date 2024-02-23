package com.example.whale.domain.delivery.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.example.whale.domain.delivery.entity.AddressEntity;
import com.example.whale.domain.delivery.entity.DeliveryEntity;
import com.example.whale.domain.delivery.model.Delivery;
import com.example.whale.domain.delivery.repository.DeliveryRepository;
import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.order.repository.OrderLineRepository;
import com.example.whale.domain.shop.dto.DeliveryOrderRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final OrderLineRepository orderLineRepository;
	private final DeliveryRepository deliveryRepository;

	public Delivery orderDelivery(DeliveryOrderRequestDTO dto) {
		OrderLineEntity orderLine = orderLineRepository.findById(dto.getOrderLineId()).orElseThrow(
			() -> new EntityNotFoundException("존재하지 않는 주문입니다.")
		);

		AddressEntity destination = orderLine.getOrder().getDestination();

		return startDelivery(saveDelivery(dto, orderLine, destination), getReceiverName(destination));
	}

	private static String getReceiverName(AddressEntity destination) {
		return destination.getReceiver().getUsername();
	}

	private Delivery startDelivery(DeliveryEntity entity, String receiverName) {
		entity.getOrderLine().updateOrderStatus(OrderStatus.DELIVERY);
		return Delivery.fromEntity(entity, receiverName);
	}

	private DeliveryEntity saveDelivery(DeliveryOrderRequestDTO dto, OrderLineEntity orderLine, AddressEntity destination) {
		return deliveryRepository.save(DeliveryEntity.of(dto.getCourierCompany(), dto.getDeliveryFee(), orderLine, destination));
	}


}