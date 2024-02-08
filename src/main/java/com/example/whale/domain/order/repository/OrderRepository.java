package com.example.whale.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.order.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
