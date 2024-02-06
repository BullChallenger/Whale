package com.example.whale.domain.product.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.shop.model.Shop;
import com.example.whale.domain.shop.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ShopRepository shopRepository;

	@Transactional
	public void register(ProductRegisterRequestDTO dto) {
		ShopEntity shop = shopRepository.findById(dto.getShopId()).orElseThrow(
			() -> new EntityNotFoundException("해당 판매자를 찾을 수 없습니다.")
		);

		Product product = Product.fromDTO(
			Shop.fromEntity(shop),
			dto.getProductName(),
			dto.getProductPrice(),
			dto.getProductStockQty(),
			dto.getProductDescription(),
			dto.getSellStatus()
		);

		productRepository.save(ProductEntity.from(product));
	}

}
