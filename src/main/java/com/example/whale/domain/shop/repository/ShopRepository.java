package com.example.whale.domain.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.shop.entity.ShopEntity;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

}
