package com.service.ttucktak.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@AllArgsConstructor
public enum BaseErrorCode {

    /**
     * 1000 ~ 1500
     * Auth Token Error
     * */
    INVALID_JWT_TOKEN(1000, "권한이 없는 토큰입니다."),

    /**
     * 9000 Database Error
     * */
    DATABASE_ERROR(9000, "Database Error");

    private final Integer status;
    private final String message;

}
