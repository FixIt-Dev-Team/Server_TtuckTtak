package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/auths")
@Slf4j
public class AuthController {
    private final JwtUtil jwtUtil;

    private final AuthService authService;

    private final OAuthService oAuthService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, OAuthService oAuthService){
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.oAuthService = oAuthService;

    }


    @PostMapping("/signup")
    public BaseResponse<PostSignUpResDto> createUsers(@RequestBody PostSignUpReqDto postSignUpReqDto){
        try{
            //이메일 validation
            if(!RegexUtil.isValidEmail(postSignUpReqDto.getEmail())) throw new BaseException(BaseErrorCode.INVALID_EMAIL);
            //비밀번호 길이 validation
            if(postSignUpReqDto.getUserPW().length() < 8) throw new BaseException(BaseErrorCode.PW_TOO_SHORT);
            if(postSignUpReqDto.getUserPW().length() > 20) throw new BaseException(BaseErrorCode.PW_TOO_LONG);
            //아이디 길이 validation
            if(postSignUpReqDto.getUserID().length() <2) throw new BaseException(BaseErrorCode.ID_TOO_SHORT);
            if(postSignUpReqDto.getUserID().length() > 10) throw new BaseException(BaseErrorCode.ID_TOO_LONG);
            //생일 형식 validation
            if(!RegexUtil.isValidDateFormat(postSignUpReqDto.getBirthday())) throw new BaseException(BaseErrorCode.INVALID_BIRTHDAY);
            PostSignUpResDto response = authService.createUsers(postSignUpReqDto);

            return new BaseResponse<>(response);

        }catch (BaseException exception){
            log.error(Arrays.toString(exception.getStackTrace()));

            return new BaseResponse<>(exception);
        }
    }

    @PostMapping("/login")
    public BaseResponse<PostLoginRes> userLogin(@RequestBody PostLoginReq req){
        try{
            TokensDto tokensDto = authService.loginToken(req.getUserId(), req.getUserPw());
            UUID userIdx = authService.loginUserIdx(req.getUserId());

            return new BaseResponse<>(new PostLoginRes(userIdx.toString(), tokensDto));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }
    @GetMapping("/oauth2/kakao")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {

        log.info(code);
        return code;
    }

    /**
     * 카카오 회원정보 조회 및 로그인처리
     * */
    @PostMapping("/oauth2/login/kakao")
    public BaseResponse<PostLoginRes> kakaoOauth2(@RequestHeader(CustomHttpHeaders.KAKAO_AUTH) String authCode){

        try{
            String authToken = oAuthService.getKakaoAccessToken(authCode);

            KakaoUserDto kakaoUserDto = oAuthService.getKakaoUserInfo(authToken);

            return new BaseResponse<>(authService.kakaoOauth2(kakaoUserDto));
            //유저 존재 여부에 따라 회원가입 처리후 로그인 처리
        }catch (BaseException exception){
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }
}
