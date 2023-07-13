package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.GoogleJwtUtil;
import com.service.ttucktak.utils.JwtUtil;
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
    public AuthController(JwtUtil jwtUtil, GoogleJwtUtil googleJwtUtil, AuthService authService, OAuthService oAuthService) {
        this.jwtUtil = jwtUtil;
        this.googleJwtUtil = googleJwtUtil;
        this.authService = authService;
        this.oAuthService = oAuthService;
    }

    @GetMapping("/exception")
    public BaseResponse<BaseException> accessExceptionHandler() {
        return new BaseResponse<>(new BaseException(BaseErrorCode.AUTH_FAILED));
    }

    @Operation(summary = "회원가입", description = "유저 회원 가입을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "이메일 형식에 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 형식에 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 형식에 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/signup")
    public BaseResponse<PostSignUpResDto> signUp(@RequestBody PostSignUpReqDto data) {
        try {
            PostSignUpResDto result = authService.signUp(data);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            log.error(e.getMessage());
            return new BaseResponse<>(e);
        }
    }

    /**
     * 로그인 처리 API
     */
    @Operation(summary = "로그인", description = "user id와 pw를 이용한 뚝딱 서비스 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "아이디나 비밀번호를 확인해주세요",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> userLogin(@RequestBody PostLoginReq req) {
        try {
            PostLoginRes result = authService.login(req.getUserId(), req.getUserPw());
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            log.error(e.getMessage());
            return new BaseResponse<>(e);
        }
    }

    /**
     * 닉네임 사용 가능 여부 확인 API
     */
    @Operation(summary = "닉네임 사용 가능 여부 확인", description = "해당 닉네임이 사용 가능한지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("nickname/{nickname}")
    public BaseResponse<GetNicknameAvailableResDto> checkNicknameAvailability(@PathVariable String nickname) {
        try {
            authService.checkNicknameExists(nickname);

            // 사용 중인 닉네임이 없다
            GetNicknameAvailableResDto result = new GetNicknameAvailableResDto(true);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            if (e.getErrorCode().equals(BaseErrorCode.ALREADY_EXIST_NICKNAME)) {
                // 사용 중인 닉네임이 있다
                GetNicknameAvailableResDto result = new GetNicknameAvailableResDto(false);
                return new BaseResponse<>(result);

            } else {
                log.error(e.getMessage());
                return new BaseResponse<>(e);
            }
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
     */
    @GetMapping("/oauth2/kakao")
    public BaseResponse<PostLoginRes> kakaoOauth2(@RequestParam("code") String authCode) {

        try {
            String authToken = oAuthService.getKakaoAccessToken(authCode);

            KakaoUserDto kakaoUserDto = oAuthService.getKakaoUserInfo(authToken);

            return new BaseResponse<>(authService.kakaoOauth2(kakaoUserDto));
            //유저 존재 여부에 따라 회원가입 처리후 로그인 처리
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 구글 회원정보 조회 및 로그인 처리
     */

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
    public BaseResponse<PostLoginRes> GoogleOauth2(@RequestHeader(CustomHttpHeaders.GOOGLE_ID) String idTokenString) {

        try {
            GoogleIdToken idToken = googleJwtUtil.CheckGoogleIdTokenVerifier(idTokenString);

            GoogleUserDto googleUserDto = null;

            if (idToken != null) {

                googleUserDto = oAuthService.getGoogleUserInfo(idToken);

            } else {
                throw new BaseException(BaseErrorCode.GOOGLE_OAUTH_EXPIRE);
            }

            return new BaseResponse<>(authService.googleOauth2(googleUserDto));
            //유저 존재 여부에 따라 회원가입 처리후 로그인 처리
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }
}