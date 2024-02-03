package com.example.whale.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultType {

    SUCCESS("0000", "Success!"),
    NOT_FOUND("4001", "Can't find your Target!"),
    SYSTEM_ERROR("9000", "System Error Occurred!");

    private final String code;
    private final String desc;

}

