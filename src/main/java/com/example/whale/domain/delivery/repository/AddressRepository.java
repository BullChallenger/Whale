package com.example.whale.domain.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.delivery.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
