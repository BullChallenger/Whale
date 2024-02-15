package com.example.whale.domain.delivery.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.delivery.dto.CreateAddressRequestDTO;
import com.example.whale.domain.delivery.dto.UpdateAddressRequestDTO;
import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.delivery.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/addresses")
public class AddressController {

	private final AddressService addressService;

	@PostMapping(value = "/save")
	public ResponseDTO<Address> saveAddressWithUserId(@RequestBody CreateAddressRequestDTO dto) {
		return ResponseDTO.ok(addressService.saveAddress(dto));
	}

	@GetMapping(value = "/{userId}")
	public ResponseDTO<List<Address>> findAddressesByUserId(@PathVariable(value = "userId") Long userId) {
		return ResponseDTO.ok(addressService.readAddressByUserId(userId));
	}

	@PutMapping(value = "/update")
	public ResponseDTO<Address> updateAddressInfo(@RequestBody UpdateAddressRequestDTO dto) {
		return ResponseDTO.ok(addressService.updateAddress(dto));
	}

	@DeleteMapping(value = "/{addressId}")
	public ResponseDTO<String> deleteAddressById(@PathVariable(value = "addressId") Long addressId) {
		addressService.deleteAddressById(addressId);
		return ResponseDTO.ok("삭제 성공");
	}

}
