package com.example.whale.domain.shop.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.shop.dto.UpdateShopInfoDTO;
import com.example.whale.domain.shop.dto.ShopRegisterRequestDTO;
import com.example.whale.domain.shop.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/shops")
public class ShopController {

	private final ShopService shopService;

	@PostMapping(value = "/register")
	public ResponseDTO<Void> register(@RequestBody ShopRegisterRequestDTO dto) {
		shopService.register(dto);
		return ResponseDTO.ok();
	}

	@GetMapping(value = "/find/{shopId}")
	public ResponseDTO<Void> findShopById(@PathVariable(value = "shopId") Long shopId) {
		shopService.findShopById(shopId);
		return ResponseDTO.ok();
	}

	@PutMapping(value = "/update")
	public ResponseDTO<Void> updateShop(@RequestBody UpdateShopInfoDTO dto) {
		shopService.updateShopInfo(dto);
		return ResponseDTO.ok();
	}

	@DeleteMapping(value = "/delete/{shopId}")
	public ResponseDTO<String> deleteShop(@PathVariable(value = "shopId") Long shopId) {
		shopService.deleteShopById(shopId);
		return ResponseDTO.ok("삭제 성공");
	}

}
