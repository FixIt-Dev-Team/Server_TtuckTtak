package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseExceptionHandler;
import com.service.ttucktak.base.ErrorResEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    BaseExceptionHandler baseExceptionHandler;

    @GetMapping("/")
    public ResponseEntity<?> testApi(){
        return baseExceptionHandler.handleBaseException(new BaseException(BaseErrorCode.INVALID_JWT_TOKEN));
    }
}
