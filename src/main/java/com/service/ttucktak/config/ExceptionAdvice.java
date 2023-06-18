package com.service.ttucktak.config;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * exception advice
 * @author LEE JIHO
 * */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    //의도된 Exception
    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseErrorCode> BaseExceptionHandle(BaseException exception) {
        log.warn("BaseException. error message: {}", exception.getMessage());
        return new BaseResponse<>(exception);
    }

    //의도되지 않은 Exception
    @ExceptionHandler(Exception.class)
    public BaseResponse<BaseErrorCode> ExceptionHandle(Exception exception) {
        log.error("Exception has occured. ", exception);
        return new BaseResponse<>(BaseErrorCode.UNEXPECTED_ERROR);
    }
}
