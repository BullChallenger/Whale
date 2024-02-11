package com.example.whale.domain.shop.repository;

import com.example.whale.domain.shop.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerEntity, String> {
}
