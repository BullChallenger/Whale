package com.example.whale.domain.product.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import com.example.whale.domain.product.dto.UpdateProductProfileDTO.UpdateProductProfileRequestDTO;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.product.repository.querydsl.CustomProductRepository;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.shop.model.Shop;
import com.example.whale.domain.shop.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final CustomProductRepository customProductRepository;
	private final ShopRepository shopRepository;

	@Transactional
	public Product register(ProductRegisterRequestDTO dto) {
		ShopEntity shop = shopRepository.findById(dto.getShopId()).orElseThrow(
			() -> new EntityNotFoundException("해당 판매자를 찾을 수 없습니다.")
		);

		Product product = Product.of(
			Shop.fromEntity(shop),
			dto.getProductName(),
			dto.getProductPrice(),
			dto.getProductStockQty(),
			dto.getProductDescription(),
			dto.getSellStatus()
		);

		productRepository.save(ProductEntity.of(product));
		return product;
	}

	@Transactional(readOnly = true)
	public Product readProductDetails(String productId) {
		return customProductRepository.readProductDetailsById(productId).orElseThrow(
			() -> new EntityNotFoundException("해당 품목을 찾을 수 없습니다.")
		);
	}

	@Transactional
	public Product updateProductProfile(UpdateProductProfileRequestDTO dto) {
		ProductEntity productEntity = productRepository.findById(dto.getProductId()).orElseThrow(
			() -> new EntityNotFoundException("해당 품목을 찾을 수 없습니다.")
		);

		if (dto.getProductName() != null) {
			productEntity.updateProductName(dto.getProductName());
		}
		if (dto.getProductDescription() != null) {
			productEntity.updateProductDescription(dto.getProductDescription());
		}

		return Product.fromEntity(productEntity);
	}

	@Transactional
	public void deleteProductById(String productId) {
		Optional<ProductEntity> opProductEntity = productRepository.findById(productId);
		if (opProductEntity.isEmpty()) {
			throw new EntityNotFoundException("해당 품목을 찾을 수 없습니다.");
		}

		productRepository.delete(opProductEntity.get());
	}

}
