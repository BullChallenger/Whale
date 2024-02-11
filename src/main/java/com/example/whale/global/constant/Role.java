package com.example.whale.global.constant;

import lombok.Getter;

@Getter
public enum Role {

    USER("USER"),
    SELLER("SELLER"),
    ADMIN("ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

}
