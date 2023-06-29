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
    AUTH_FAILED(HttpStatus.BAD_REQUEST.value(), "권한 인증에 실패하였습니다, 토큰을 갱신해주세요"),
    PW_TOO_SHORT(HttpStatus.BAD_REQUEST.value(), "비밀번호가 너무 짧습니다."),
    PW_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "비밀번호가 너무 깁니다."),
    ID_TOO_SHORT(HttpStatus.BAD_REQUEST.value(), "유저 아이디가 너무 짧습니다."),
    ID_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "유저 아이디가 너무 깁니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST.value(), "이메일 형식에 맞지 않습니다."),
    INVALID_BIRTHDAY(HttpStatus.BAD_REQUEST.value(), "생일 형식에 맞지 않습니다 yyyy-MM-dd"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST.value(), "아이디나 비밀번호를 확인해주세요"),


    /**
     * 401 : UNATHORIZED
     *
     */
    GOOGLE_OAUTH_EXPIRE(HttpStatus.UNAUTHORIZED.value(), "구글 로그인 중 ID토큰 검증 실패 오류발생"),

    /**
     * 404 - NOT FOUND
     * */
    KAKAO_EMAIL_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "카카오 이메일 동의가 필요합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저가 존재하지 않습니다."),
    /**
     * 500 INTERNAL SERVER ERROR
     * */
    KAKAO_OAUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "카카오 로그인 중 오류발생 서버에 문의"),
    GOOGLE_OAUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "구글 로그인 중 GoogleIDToken Payload 과정에서 오류발생 서버에 문의"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database Error"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생하였습니다."),
    GOOGLE_GENERALSECURITY_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "구글 JWT 토큰 인증중 구글 시큐리티 문제가 발생하였습니다."),
    GOOGLE_IOEXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "구글 JWT 토큰 인증중 IO 문제가 발생하였습니다.");
    /**
     *
     * */


    private final Integer status;
    private final String message;

}
