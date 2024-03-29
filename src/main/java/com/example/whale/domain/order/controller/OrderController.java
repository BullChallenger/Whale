package com.example.whale.domain.order.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.order.dto.CreatePurchaseOrderRequestDTO;
import com.example.whale.domain.order.dto.ReadOrderLinesResponseDTO;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orders")
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping(value = "/order")
    public ResponseDTO<Order> order(@RequestBody CreatePurchaseOrderRequestDTO dto) {
        return ResponseDTO.ok(orderService.createPurchaseOrder(dto));
    }

    @GetMapping(value = "/read/{customerId}")
    public ResponseDTO<Page<Order>> readOrdersByCustomerId(@PathVariable(value = "customerId") Long customerId,
                                                                 Pageable pageable) {
        return ResponseDTO.ok(orderService.readOrdersByCustomerId(customerId, pageable));
    }

    @GetMapping(value = "/read/orderLines/{orderId}")
    public ResponseDTO<List<ReadOrderLinesResponseDTO>> readOrderLinesByOrderId(@PathVariable(value = "orderId") String orderId) {
        return ResponseDTO.ok(orderService.readOrderLinesByOrderId(orderId));
    }

}
