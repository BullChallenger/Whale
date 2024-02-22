package com.example.whale.global.util.converter;

import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.example.whale.global.constant.Role;

@Convert
public class RoleListConverter implements AttributeConverter<List<Role>, String> {

    private static final String SPLIT = ", ";

    @Override
    public String convertToDatabaseColumn(List<Role> roles) {
        List<String> authorities = roles.stream().map(Role::getAuthority).toList();
        return String.join(SPLIT, authorities);
    }

    @Override
    public List<Role> convertToEntityAttribute(String storedRoles) {
        List<Role> roles = Arrays.stream(storedRoles.split(SPLIT)).map(Role::valueOf).toList();
        return roles;
    }

}
