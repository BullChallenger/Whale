package com.example.whale.domain.delivery.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.delivery.model.Delivery;
import com.example.whale.domain.delivery.service.DeliveryService;
import com.example.whale.domain.shop.dto.DeliveryOrderRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping(value = "/delivery/order")
	public ResponseDTO<Delivery> orderDelivery(@RequestBody DeliveryOrderRequestDTO dto) {
		return ResponseDTO.ok(deliveryService.orderDelivery(dto));
	}

}
