package com.example.whale.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

}
