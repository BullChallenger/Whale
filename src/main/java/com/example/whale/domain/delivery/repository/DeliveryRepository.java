package com.example.whale.domain.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.delivery.entity.DeliveryEntity;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {

}
