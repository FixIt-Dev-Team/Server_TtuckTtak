package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.PostSigninReqDto;
import com.service.ttucktak.dto.auth.PostSigninResDto;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;
import io.swagger.annotations.ApiOperation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auths")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtility jwtUtility;

    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtility jwtUtility, AuthService authService){
        this.jwtUtility = jwtUtility;
        this.authService = authService;
    }

    @ApiOperation(value = "자체 회원가입", notes = "자체 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "1501", description = "비밀번호가 너무 짧습니다."),
            @ApiResponse(responseCode = "1502", description = "비밀번호가 너무 깁니다."),
            @ApiResponse(responseCode = "1503", description = "유저 아이디가 너무 짧습니다"),
            @ApiResponse(responseCode = "1504", description = "유저 아이디가 너무 깁니다."),
            @ApiResponse(responseCode = "1505", description = "이메일 형식에 맞지 않습니다."),
            @ApiResponse(responseCode = "1506", description = "생일 형식에 맞지 않습니다 yyy-MM-dd")
    })
    @PostMapping("/signup")
    public BaseResponse<PostSigninResDto> createUsers(@RequestBody PostSigninReqDto postSigninReqDto){
        try{
            //이메일 validation
            if(!RegexUtility.isValidEmail(postSigninReqDto.getEmail())) throw new BaseException(BaseErrorCode.INVALID_EMAIL);
            //비밀번호 길이 validation
            if(postSigninReqDto.getUserPW().length() < 8) throw new BaseException(BaseErrorCode.PW_TOO_SHORT);
            if(postSigninReqDto.getUserPW().length() > 20) throw new BaseException(BaseErrorCode.PW_TOO_LONG);
            //아이디 길이 validation
            if(postSigninReqDto.getUserID().length() <2) throw new BaseException(BaseErrorCode.ID_TOO_SHORT);
            if(postSigninReqDto.getUserID().length() > 10) throw new BaseException(BaseErrorCode.ID_TOO_LONG);
            //생일 형식 validation
            if(!RegexUtility.isValidDateFormat(postSigninReqDto.getBirthday())) throw new BaseException(BaseErrorCode.INVALID_BIRTHDAY);
            System.out.println(postSigninReqDto.getUserID());
            PostSigninResDto response = authService.createUsers(postSigninReqDto);

            return new BaseResponse<>(response);

        }catch (BaseException exception){
            logger.error(Arrays.toString(exception.getStackTrace()));

            return new BaseResponse<>(exception);
        }
    }
}
