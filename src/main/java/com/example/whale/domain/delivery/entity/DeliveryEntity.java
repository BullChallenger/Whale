package com.example.whale.domain.delivery.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.example.whale.domain.delivery.constant.CourierCompany;
import com.example.whale.domain.order.entity.OrderLineEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryEntity {

	@Id
	private String id; // ${배송사 코드}:${송장번호}

	@Enumerated(EnumType.STRING)
	private CourierCompany courierCompany; // 택배사
	private String invoiceNumber; // 송장번호
	private BigDecimal fee; // 택배 요금

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private OrderLineEntity orderLine;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private AddressEntity destination;

	@Builder
	public DeliveryEntity(CourierCompany courierCompany, BigDecimal fee,
						  OrderLineEntity orderLine, AddressEntity destination
	) {
		String invoiceNumber = generateInvoiceNumber();
		this.id = courierCompany.getCode() + invoiceNumber;
		this.courierCompany = courierCompany;
		this.invoiceNumber = invoiceNumber;
		this.fee = fee;
		this.orderLine = orderLine;
		this.destination = destination;
	}

	private String generateInvoiceNumber() {
		StringBuilder sb = new StringBuilder();

		final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String SUFFIX = "-KR";
		final int ALPHABET_LENGTH = ALPHABET.length();

		sb.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		generateRandomAlphabet(sb, ALPHABET, ALPHABET_LENGTH);
		generateRandomNumber(sb);

		return sb.append(SUFFIX).toString();
	}

	private static void generateRandomAlphabet(StringBuilder sb, String ALPHABET, int ALPHABET_LENGTH) {
		for (int i = 0; i < 4; i++) {
			sb.append(ALPHABET.charAt((int) (Math.random() * ALPHABET_LENGTH)));
		}
	}

	private static void generateRandomNumber(StringBuilder sb) {
		for (int i = 0; i < 4; i++) {
			sb.append((int) (Math.random() * 10));
		}
	}

	public static DeliveryEntity of(CourierCompany courierCompany, BigDecimal fee,
									OrderLineEntity orderLine, AddressEntity destination
	) {
		return DeliveryEntity.builder()
			.courierCompany(courierCompany)
			.fee(fee)
			.orderLine(orderLine)
			.destination(destination)
			.build();
	}

	public void test() {

	}

}
