package com.service.ttucktak.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseErrorCode {

    /**
     * 1000 ~ 1500
     * Auth Token Error
     * */
    INVALID_JWT_TOKEN(1000, "권한이 없는 토큰입니다."),
    CORRUPTED_TOKEN(1001, "유효하지않은 토큰입니다."),
    EXPIRED_TOKEN(1002, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1003, "지원하지 않는 토큰입니다."),
    TOKEN_NOT_EXIST(1004, "토큰이 존재하지 않습니다."),

    /**
     * 9000 Database Error
     * */
    DATABASE_ERROR(9000, "Database Error");

    private final Integer status;
    private final String message;

}
