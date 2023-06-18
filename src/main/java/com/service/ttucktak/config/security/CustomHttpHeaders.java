package com.service.ttucktak.config.security;

import org.springframework.http.HttpHeaders;

/**
 * Http Header 이름 Custom Class
 * @author LEE JIHO
 * */
public class CustomHttpHeaders extends HttpHeaders {
    public static final String REFRESH = "Refresh";
    public static final String KAKAO_AUTH = "Kakao-auth-code";
}
