package com.example.whale.domain.product.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.example.whale.domain.product.constant.SellStatus;
import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import com.example.whale.domain.product.dto.UpdateProductProfileDTO.UpdateProductProfileRequestDTO;
import com.example.whale.domain.product.entity.ProductEntity;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.repository.ProductRepository;
import com.example.whale.domain.product.repository.querydsl.CustomProductRepository;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.shop.repository.ShopRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private static final String DUMMY_PRODUCT_ID = "IIIIIIIIIII";

	@Mock
	private ShopRepository shopRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private CustomProductRepository customProductRepository;
	@InjectMocks
	private ProductService productService;

	private ProductRegisterRequestDTO returnProductRegisterDTOValue() {
		return new ProductRegisterRequestDTO(
			1L,
			"Dummy Product",
			BigDecimal.valueOf(1000L),
			10L,
			"This product is dummy",
			SellStatus.WAITING_FOR_SALE
		);
	}

	private UpdateProductProfileRequestDTO returnProductUpdateDTOValue() {
		return new UpdateProductProfileRequestDTO(
			DUMMY_PRODUCT_ID,
			"Updated Product Name",
			"Updated Product Description"
		);
	}

	private ShopEntity returnShopEntityValue() {
		return ShopEntity.of(
			"testShop",
			"this shop is dummy"
		);
	}

	private Product returnProductValue() {
		return Product.fromEntity(
			ProductEntity.builder()
				.productId(DUMMY_PRODUCT_ID)
				.provider(returnShopEntityValue())
				.productName("Dummy Product")
				.productStockQty(10L)
				.productPrice(BigDecimal.valueOf(1000L))
				.productDescription("This product is dummy")
				.sellStatus(SellStatus.WAITING_FOR_SALE)
			.build()
		);
	}

	private ProductEntity returnProductEntityValue() {
		return ProductEntity.builder()
				.productId(DUMMY_PRODUCT_ID)
				.provider(returnShopEntityValue())
				.productName("Dummy Product")
				.productStockQty(10L)
				.productPrice(BigDecimal.valueOf(1000L))
				.productDescription("This product is dummy")
				.sellStatus(SellStatus.WAITING_FOR_SALE)
			.build();
	}

	@Test
	@Transactional(readOnly = true)
	@DisplayName(value = "[성공]_제품_저장")
	void registerProductSuccessTest() {
		// given
		Mockito.when(shopRepository.findById(any())).thenReturn(Optional.ofNullable(returnShopEntityValue()));
		Mockito.when(productRepository.save(any())).thenReturn(returnProductEntityValue());

		// when
		Product product = productService.register(returnProductRegisterDTOValue());

		// then
		assertThat(product.getProductId()).isNotBlank();
		assertThat(product.getProductName()).isEqualTo("Dummy Product");
		assertThat(product.getProvider().getShopName()).isEqualTo("testShop");
	}

	@Test
	@Transactional(readOnly = true)
	@DisplayName(value = "[성공]_제품_정보_가져오기")
	void readProductDetailsSuccessTest() {
		// given
		Mockito.when(customProductRepository.readProductDetailsById(DUMMY_PRODUCT_ID)).thenReturn(
			Optional.ofNullable(returnProductValue())
		);

		// when
		Product product = productService.readProductDetails(DUMMY_PRODUCT_ID);

		// then
		assertThat(product.getProductId()).isEqualTo(DUMMY_PRODUCT_ID);
		assertThat(product.getProvider().getShopName()).isEqualTo("testShop");
	}

	@Test
	@Transactional
	@DisplayName(value = "[성공]_제품_정보_업데이트_이름_및_설명")
	void updateProductProfile() {
		// given
		Mockito.when(productRepository.findById(DUMMY_PRODUCT_ID)).thenReturn(
			Optional.ofNullable(returnProductEntityValue())
		);

		// when
		Product product = productService.updateProductProfile(returnProductUpdateDTOValue());

		// then
		assertThat(product.getProductName()).isEqualTo("Updated Product Name");
		assertThat(product.getProductDescription()).isEqualTo("Updated Product Description");
	}

	@Test
	@Transactional
	@DisplayName(value = "[성공]_제품_삭제")
	void deleteProduct() {
		// given
		Mockito.doReturn(true).when(customProductRepository).isProductExists(DUMMY_PRODUCT_ID);
		Mockito.doNothing().when(productRepository).deleteById(DUMMY_PRODUCT_ID);

		// when
		productService.deleteProductById(DUMMY_PRODUCT_ID);

		// then
		Mockito.verify(customProductRepository, Mockito.times(1)).isProductExists(DUMMY_PRODUCT_ID);
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(DUMMY_PRODUCT_ID);
	}

}