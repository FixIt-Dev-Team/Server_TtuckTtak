package com.service.ttucktak.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    BaseErrorCode errorCode;
}
