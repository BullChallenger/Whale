package com.example.whale.domain.common.controller;

import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.common.dto.ResultObject;

public abstract class BaseController {

    protected <T> ResponseDTO<T> ok() { return ok(null, ResultObject.getSuccess()); }

    protected <T> ResponseDTO<T> ok(T data) {
        return ok(data, ResultObject.getSuccess());
    }

    protected <T> ResponseDTO<T> ok(T data, ResultObject result) {
        ResponseDTO<T> obj = ResponseDTO.<T>resultDataBuilder()
                .data(data)
                .result(result)
                .build();

        return obj;
    }

}
