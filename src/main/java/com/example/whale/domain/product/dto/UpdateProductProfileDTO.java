package com.example.whale.domain.product.dto;

import lombok.Getter;

public class UpdateProductProfileDTO {

	@Getter
	public static class UpdateProductProfileRequestDTO {

		private final String productId;
		private final String productName;
		private final String productDescription;

		public UpdateProductProfileRequestDTO(String productId, String productName, String productDescription) {
			this.productId = productId;
			this.productName = productName;
			this.productDescription = productDescription;
		}

	}

}
