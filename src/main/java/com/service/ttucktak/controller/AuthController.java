package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.KakaoUserDto;
import com.service.ttucktak.dto.auth.PostSigninReqDto;
import com.service.ttucktak.dto.auth.PostSigninResDto;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auths")
@Slf4j
public class AuthController {
    private final JwtUtility jwtUtility;

    private final AuthService authService;

    private final OAuthService oAuthService;

    @Autowired
    public AuthController(JwtUtility jwtUtility, AuthService authService, OAuthService oAuthService){
        this.jwtUtility = jwtUtility;
        this.authService = authService;
        this.oAuthService = oAuthService;

    }


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
            log.error(Arrays.toString(exception.getStackTrace()));

            return new BaseResponse<>(exception);
        }
    }

    @GetMapping("/oauth2/kakao")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {

        log.info(code);
        return code;
    }

    /**
     * 카카오 회원가입 및 로그인처리
     * */
    @PostMapping("/oauth2/kakao/data")
    public KakaoUserDto kakaoOauth2(@RequestHeader("AuthorizationCode") String authCode){
        log.info("in");

        String authToken = oAuthService.getKakaoAccessToken(authCode);
        KakaoUserDto kakaoUserDto = null;
        try{
            kakaoUserDto = oAuthService.getKakaoUserInfo(authToken);
        }catch (BaseException exception){
            log.error(exception.getMessage());
        }
        
        return kakaoUserDto;
    }
}
