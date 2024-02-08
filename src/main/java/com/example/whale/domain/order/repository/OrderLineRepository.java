package com.example.whale.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.order.entity.OrderLineEntity;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, String> {

}
