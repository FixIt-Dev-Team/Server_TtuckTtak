package com.service.ttucktak.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseErrorCode {

    /**
     * 404 - NOT FOUND
     * */
    KAKAO_EMAIL_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "카카오 이메일 동의가 필요합니다."),

    /**
     * 500 INTERNAL SERVER ERROR
     * */
    KAKAO_OAUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "카카오 로그인 중 오류발생 서버에 문의"),
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
     * 1501 ~ 1999
     * Auth Controller Error
     * */
    PW_TOO_SHORT(1501, "비밀번호가 너무 짧습니다."),
    PW_TOO_LONG(1502, "비밀번호가 너무 깁니다."),
    ID_TOO_SHORT(1503, "유저 아이디가 너무 짧습니다"),
    ID_TOO_LONG(1504, "유저 아이디가 너무 깁니다."),
    INVALID_EMAIL(1505, "이메일 형식에 맞지 않습니다."),
    INVALID_BIRTHDAY(1506, "생일 형식에 맞지 않습니다 yyy-MM-dd"),
    /**
     * 9000 Database Error
     * */
    DATABASE_ERROR(9000, "Database Error");

    private final Integer status;
    private final String message;

}
