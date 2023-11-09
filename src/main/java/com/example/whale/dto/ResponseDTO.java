package com.example.whale.dto;

import com.example.whale.exception.BaseException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDTO<T> {

    private ResultObject result;

    private T data;

    @Builder(builderMethodName = "resultBuilder")
    public ResponseDTO(ResultObject result) { this.result = result; }

    @Builder(builderMethodName = "resultDataBuilder")
    public ResponseDTO(ResultObject result, T data) {
        this.result = result;
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok() {
        return new ResponseDTO<>(ResultObject.getSuccess());
    }

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(ResultObject.getSuccess(), data);
    }

    public ResponseDTO(BaseException ex) {
        this.result = new ResultObject(ex);
    }

}
