package com.service.ttucktak.config;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// Todo: 파일 위치 고민해보기

/**
 * exception advice
 * @author LEE JIHO
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * exception handler - constraint violation exception
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected BaseResponse<BaseException> handleConstraintViolationException(ConstraintViolationException e) {
        // 유효하지 않은 값들의 오류를 모아 로그로 출력 하고 반환한다
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.error(message);

        return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), message, null);
    }

    /**
     * exception handler - method argument not valid exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected BaseResponse<BaseException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 유효하지 않은 값들의 오류를 모아 로그로 출력 하고 반환한다
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error(message);

        return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), message, null);
    }

    // 의도된 Exception
    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseErrorCode> handleBaseException(BaseException exception) {
        log.warn("BaseException. error message: {}", exception.getMessage());
        return new BaseResponse<>(exception);
    }

    // 의도되지 않은 Exception
    @ExceptionHandler(Exception.class)
    public BaseResponse<BaseErrorCode> handleException(Exception exception) {
        log.error("Exception has raised: {}", exception.getMessage());
        return new BaseResponse<>(new BaseException(BaseErrorCode.UNEXPECTED_ERROR));
    }
}
