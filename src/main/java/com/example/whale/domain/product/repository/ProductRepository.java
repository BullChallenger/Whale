package com.example.whale.domain.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @EntityGraph(attributePaths = {"provider"})
    @Query(value = "select p from ProductEntity p where p.id in :productIds")
    List<ProductEntity> findProductsByIds(List<String> productIds);

}
