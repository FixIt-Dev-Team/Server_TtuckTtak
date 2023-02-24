package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseExceptionHandler;
import com.service.ttucktak.dto.auth.PostSigninReqDto;
import com.service.ttucktak.dto.auth.PostSigninResDto;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auths")
public class AuthController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtility jwtUtility;

    private final RegexUtility regexUtility;

    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtility jwtUtility, RegexUtility regexUtility, AuthService authService){
        this.jwtUtility = jwtUtility;
        this.regexUtility = regexUtility;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUsers(@RequestBody PostSigninReqDto postSigninReqDto){
        try{
            //이메일 validation
            //비밀번호 길이 validation
            //아이디 길이 validation
            //생일 형식 validation
            System.out.println(postSigninReqDto.getUserID());
            PostSigninResDto response = authService.createUsers(postSigninReqDto);

            return ResponseEntity.ok(response);

        }catch (BaseException exception){
            logger.error(exception.getMessage());

            return BaseExceptionHandler.handleBaseException(exception);
        }
    }
}
