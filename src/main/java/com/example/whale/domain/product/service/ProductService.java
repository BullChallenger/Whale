package com.example.whale.domain.product.service;

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

		ProductEntity product = productRepository.save(ProductEntity.of(
				shop,
				dto.getProductName(),
				dto.getProductStockQty(),
				dto.getProductPrice(),
				dto.getProductDescription(),
				dto.getSellStatus()
		));

		return Product.fromEntity(product);
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
		if (!customProductRepository.isProductExists(productId)) {
			throw new EntityNotFoundException("해당 품목을 찾을 수 없습니다.");
		}

		productRepository.deleteById(productId);
	}

}
