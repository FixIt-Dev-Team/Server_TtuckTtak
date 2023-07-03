package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.GoogleJwtUtil;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/auths")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
@Slf4j
@Tag(name = "회원 인증 API")
public class AuthController {
    private final JwtUtil jwtUtil;

    private final GoogleJwtUtil googleJwtUtil;

    private final AuthService authService;

    private final OAuthService oAuthService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, GoogleJwtUtil googleJwtUtil, AuthService authService, OAuthService oAuthService){
        this.jwtUtil = jwtUtil;
        this.googleJwtUtil = googleJwtUtil;
        this.authService = authService;
        this.oAuthService = oAuthService;

    }

    @GetMapping("/exception")
    public BaseResponse<BaseException> accessExceptionHandler(){
        return new BaseResponse<>(new BaseException(BaseErrorCode.AUTH_FAILED));
    }


    @Operation(summary = "회원가입", description = "유저 회원 가입을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "이메일 형식에 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호가 너무 짧습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호가 너무 깁니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "유저 아이디가 너무 짧습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "유저 아이디가 너무 깁니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "생일 형식에 맞지 않습니다 yyyy-MM-dd",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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

    /**
     * 로그인 처리 API
     * */
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

//    /**
//     * 카카오 태스트
//     * */
//    @GetMapping("oauth2/kakao/test")
//    public String kakaoTest(@RequestParam("code") String code){
//        return code;
//    }
    /**
     * 카카오 회원정보 조회 및 로그인처리
     * */
    @GetMapping("/oauth2/kakao")
    public BaseResponse<PostLoginRes> kakaoOauth2(@RequestParam("code") String authCode){

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

    /**
     * 구글 회원정보 조회 및 로그인 처리
     * */

    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "구글 로그인 중 ID 토큰 검증 실패 오류발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 로그인 중 GoogleIDToken Payload 과정에서 오류발생 서버에 문의",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 JWT 토큰 인증중 구글 시큐리티 문제가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 JWT 토큰 인증중 IO 문제가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/oauth2/google")
    public BaseResponse<PostLoginRes> GoogleOauth2(@RequestHeader(CustomHttpHeaders.GOOGLE_ID) String idTokenString){

        try{
            GoogleIdToken idToken = googleJwtUtil.CheckGoogleIdTokenVerifier(idTokenString);

            GoogleUserDto googleUserDto = null;

            if (idToken != null) {

                googleUserDto = oAuthService.getGoogleUserInfo(idToken);

            } else {
                throw new BaseException(BaseErrorCode.GOOGLE_OAUTH_EXPIRE);
            }

            return new BaseResponse<>(authService.googleOauth2(googleUserDto));
            //유저 존재 여부에 따라 회원가입 처리후 로그인 처리
        }catch (BaseException exception){
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }
}
