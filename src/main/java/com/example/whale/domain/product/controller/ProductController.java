package com.example.whale.domain.product.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.product.dto.ProductRegisterRequestDTO;
import com.example.whale.domain.product.dto.UpdateProductProfileDTO.UpdateProductProfileRequestDTO;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "/register")
	public ResponseDTO<Product> register(@RequestBody ProductRegisterRequestDTO dto) {
		return ResponseDTO.ok(productService.register(dto));
	}

	@GetMapping(value = "/read/{productId}")
	public ResponseDTO<Product> findProductById(@PathVariable(value = "productId") String productId) {
		return ResponseDTO.ok(productService.readProductDetails(productId));
	}

	@PutMapping(value = "/update")
	public ResponseDTO<Product> updateProduct(@RequestBody UpdateProductProfileRequestDTO dto) {
		return ResponseDTO.ok(productService.updateProductProfile(dto));
	}

	@DeleteMapping(value = "/delete/{productId}")
	public ResponseDTO<String> deleteProductById(@PathVariable(value = "productId") String productId) {
		productService.deleteProductById(productId);
		return ResponseDTO.ok("삭제 성공");
	}

}
