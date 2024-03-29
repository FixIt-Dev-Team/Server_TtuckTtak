package com.service.ttucktak.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> {

    private boolean isSuccess;
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public BaseResponse(T data) {
        this.isSuccess = true;
        this.code = 200;
        this.message = "요청에 성공하였습니다.";
        this.data = data;
    }

    public BaseResponse(BaseException exception){
        this.isSuccess = false;
        this.code = exception.errorCode.getStatus();
        this.message = exception.errorCode.getMessage();
    }
}
