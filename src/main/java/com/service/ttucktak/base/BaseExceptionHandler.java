package com.service.ttucktak.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResEntity> handleBaseException(BaseException exception) {
        return ErrorResEntity.toResponseEntity(exception.getErrorCode());
    }
}
