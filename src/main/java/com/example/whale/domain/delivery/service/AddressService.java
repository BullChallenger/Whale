package com.example.whale.domain.delivery.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.whale.domain.delivery.dto.CreateAddressRequestDTO;
import com.example.whale.domain.delivery.dto.UpdateAddressRequestDTO;
import com.example.whale.domain.delivery.entity.AddressEntity;
import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.delivery.repository.AddressRepository;
import com.example.whale.domain.delivery.repository.querydsl.CustomAddressRepository;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
	
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final CustomAddressRepository customAddressRepository;

	@Transactional
	public Address saveAddress(CreateAddressRequestDTO dto) {
		UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(
			() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.")
		);

		AddressEntity addressEntity = addressRepository.save(
			AddressEntity.of(
				user,
				dto.getZipcode(),
				dto.getAddress(),
				dto.getDetailAddress()
			)
		);

		return Address.fromEntity(addressEntity);
	}

	@Transactional(readOnly = true)
	public List<Address> readAddressByUserId(Long userId) {
		return customAddressRepository.findAddressesByUserId(userId);
	}

	@Transactional
	public Address updateAddress(UpdateAddressRequestDTO dto) {
		AddressEntity address = addressRepository.findById(dto.getAddressId()).orElseThrow(
			() -> new EntityNotFoundException("해당 주소를 찾을 수 없습니다")
		);

		if (dto.getZipcode() != null) {
			address.updateZipcode(dto.getZipcode());
		}

		if (dto.getAddress() != null) {
			address.updateAddress(dto.getAddress());
		}

		if (dto.getDetailAddress() != null) {
			address.updateDetailAddress(dto.getDetailAddress());
		}

		return Address.fromEntity(address);
	}

	@Transactional
	public void deleteAddressById(Long addressId) {
		addressRepository.deleteById(addressId);
	}

}
