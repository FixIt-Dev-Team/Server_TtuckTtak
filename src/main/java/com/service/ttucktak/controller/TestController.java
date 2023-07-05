package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check")
public class TestController {

    @GetMapping("/health")
    public String healthTest(){
        return "OK";
    }

    @GetMapping("/exception")
    public BaseResponse<String> exceptionTest(){
        return new BaseResponse<>(new BaseException(BaseErrorCode.DATABASE_ERROR));
    }
}
