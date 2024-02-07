package com.example.whale.domain.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.whale.domain.shop.dto.UpdateShopInfoDTO;
import com.example.whale.domain.shop.dto.ShopRegisterRequestDTO;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.shop.repository.ShopRepository;
import com.example.whale.domain.shop.repository.querydsl.CustomShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService {

	private final ShopRepository shopRepository;
	private final CustomShopRepository customShopRepository;

	@Transactional
	public void register(ShopRegisterRequestDTO dto) {
		if (customShopRepository.isSameShopNameAlreadyExists(dto.getShopName())) {
			throw new IllegalArgumentException("동일한 이름을 사용하는 판매자가 이미 존재합니다.");
		}
		shopRepository.save(ShopEntity.of(dto.getShopName(), dto.getShopDescription()));

		// TODO: 해당 판매자와 연결된 Whale Wallet 추가
	}

	public void findShopById(Long shopId) {
		ShopEntity shop = shopRepository.findById(shopId).orElseThrow(
			() -> new EntityNotFoundException("해당 판매자를 찾을 수 없습니다.")
		);

		// TODO: Response DTO 만들기
	}

	public void updateShopInfo(UpdateShopInfoDTO dto) {
		ShopEntity shop = shopRepository.findById(dto.getShopId()).orElseThrow(
			() -> new EntityNotFoundException("해당 판매자를 찾을 수 없습니다.")
		);

		if (dto.getShopName() != null) {
			shop.updateShopName(dto.getShopName());
		}
		if (dto.getShopDescription() != null) {
			shop.updateShopDescription(dto.getShopDescription());
		}
	}

	public void deleteShopById(Long shopId) {
		if (!customShopRepository.isShopExists(shopId)) {
			throw new EntityNotFoundException("해당 판매자를 찾을 수 없습니다.");
		}

		shopRepository.deleteById(shopId);
	}

}
