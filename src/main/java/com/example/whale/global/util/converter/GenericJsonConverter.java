package com.example.whale.global.util.converter;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericJsonConverter<T> implements AttributeConverter<T, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(T attribute) {
		if (ObjectUtils.isEmpty(attribute)) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T convertToEntityAttribute(String jsonData) {
		if (StringUtils.hasText(jsonData)) {
			try {
				return objectMapper.readValue(jsonData, new TypeReference<T>() {});
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return null;
	}

}
