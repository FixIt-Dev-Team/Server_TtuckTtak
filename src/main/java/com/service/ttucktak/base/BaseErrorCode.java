package com.service.ttucktak.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseErrorCode {



    /**
     * 400 : BAD REQUEST
     *
     */
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST.value(), "권한이 없는 토큰입니다."),

    PW_TOO_SHORT(HttpStatus.BAD_REQUEST.value(), "비밀번호가 너무 짧습니다."),
    PW_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "비밀번호가 너무 깁니다."),
    ID_TOO_SHORT(HttpStatus.BAD_REQUEST.value(), "유저 아이디가 너무 짧습니다"),
    ID_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "유저 아이디가 너무 깁니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST.value(), "이메일 형식에 맞지 않습니다."),
    INVALID_BIRTHDAY(HttpStatus.BAD_REQUEST.value(), "생일 형식에 맞지 않습니다 yyyy-MM-dd"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST.value(), "아이디나 비밀번호를 확인해주세요"),

    /**
     * 404 - NOT FOUND
     * */
    KAKAO_EMAIL_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "카카오 이메일 동의가 필요합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저가 존재하지 않습니다."),
    /**
     * 500 INTERNAL SERVER ERROR
     * */
    KAKAO_OAUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "카카오 로그인 중 오류발생 서버에 문의"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database Error");
    /**
     *
     * */


    private final Integer status;
    private final String message;

}
