package com.service.ttucktak.controller;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.service.EmailService;
import com.service.ttucktak.utils.GoogleJwtUtil;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import com.service.ttucktak.utils.RegexUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auths")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원 인증 API")
public class AuthController {
    private final JwtUtil jwtUtil;

    private final GoogleJwtUtil googleJwtUtil;

    private final AuthService authService;

    private final OAuthService oAuthService;

    private final EmailService emailService;

    @GetMapping("/exception")
    public BaseResponse<BaseException> accessExceptionHandler() {
        return new BaseResponse<>(new BaseException(BaseErrorCode.AUTH_FAILED));
    }

    /**
     * 회원가입
     */
    @Operation(summary = "회원가입", description = "서비스에 회원가입")
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
            // 이메일 형식 validation
            // 앱을 통해 회원가입 할 때는 이메일 인증은 사전에 한 상태
            // API를 통해 입력받을 때 이메일 인증은 못 하더라도 최소한 이메일 형태로 받을 수 있게 이메일 형식만 체크
            // 이메일 형식에 맞지 않는 경우 invalid email exception
            String email = data.getUserId();
            if (!RegexUtil.isValidEmailFormat(email)) throw new BaseException(BaseErrorCode.INVALID_EMAIL_FORMAT);

            // 비밀번호 형식 validation
            // 비밀번호 형식에 맞지 않는 경우 invalid pw format exception
            String pw = data.getUserPw();
            if (!RegexUtil.isValidPwFormat(pw)) throw new BaseException(BaseErrorCode.INVALID_PW_FORMAT);

            // 닉네임 형식 validation
            // 닉네임 형식에 맞지 않는 경우 invalid nickname format exception
            String nickname = data.getNickname();
            if (!RegexUtil.isValidNicknameFormat(nickname))
                throw new BaseException(BaseErrorCode.INVALID_NICKNAME_FORMAT);

            PostSignUpResDto result = authService.signUp(data);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            log.error(e.getMessage());
            return new BaseResponse<>(e);
        }
    }

    /**
     * 로그인
     */
    @Operation(summary = "로그인", description = "사용자의 id와 pw를 이용해 서비스 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "아이디나 비밀번호를 확인해주세요",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
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

    @Operation(summary = "엑세스 토큰 비활성(서비스 내부 로그아웃)", description = "사용자의 서비스 로그아웃 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "아이디나 비밀번호를 확인해주세요",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/logout")
    public BaseResponse<PostLogoutRes> userLogout(@RequestBody PostLogoutReq req) {

        UUID userIdx;

        try{
            userIdx = UUID.fromString(req.getUserIdx());
            return new BaseResponse<>(authService.logout(userIdx));

        } catch (BaseException e) {
            e.printStackTrace();
            return new BaseResponse<>(e);
        } catch (Exception e){
            e.getCause();
            log.error(e.getMessage());
            return new BaseResponse<>(new BaseException(BaseErrorCode.UUID_ERROR));
        }

    }

    /**
     * 이메일 인증
     */
    @Operation(summary = "이메일 인증", description = "유효한 이메일 인지 인증 코드를 보내 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/email-confirm")
    public BaseResponse<PostEmailConfirmResDto> emailConfirm(@RequestParam String to) {
        try {
            String ePw = emailService.sendSimpleMessage(to);
            PostEmailConfirmResDto result = new PostEmailConfirmResDto(ePw);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            e.printStackTrace();
            return new BaseResponse<>(e);
        }
    }

    /**
     * 닉네임 사용 가능 여부 확인
     */
    @Operation(summary = "닉네임 사용 가능 여부 확인", description = "해당 닉네임이 현재 사용 가능한지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("nickname")
    public BaseResponse<GetNicknameAvailableResDto> checkNicknameAvailability(@RequestParam("nickname") String nickname) {
        try {
            boolean isAvailable = !authService.checkNicknameExists(nickname);
            GetNicknameAvailableResDto result = new GetNicknameAvailableResDto(isAvailable);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            log.error(e.getMessage());
            return new BaseResponse<>(e);
        }
    }

    /**
     * 카카오 계정을 통해 로그인
     */
    @Operation(summary = "카카오 계정을 통해 로그인", description = "사용자의 카카오 인가 코드를 사용하여 서비스에 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/oauth2/kakao")
    public BaseResponse<PostLoginRes> kakaoOauth2(@RequestParam("code") String authCode) {

        try {
            String authToken = oAuthService.getKakaoAccessToken(authCode);

            SocialAccountUserInfo data = oAuthService.getKakaoUserInfo(authToken);

            return new BaseResponse<>(authService.loginWithSocialAccount(data, AccountType.KAKAO));
            //유저 존재 여부에 따라 회원가입 처리후 로그인 처리
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 구글 계정을 통해 서비스 로그인
     */
    @Operation(summary = "구글 계정을 통해 로그인", description = "사용자의 구글 id token을 사용하여 서비스에 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "구글 로그인 중 ID 토큰 검증 실패. 오류 발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 로그인 중 GoogleIDToken Payload 과정에서 오류 발생. 서버에 문의.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 JWT 토큰 인증 중 구글 시큐리티 문제 발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "구글 JWT 토큰 인증 중 IO 문제 발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/oauth2/google")
    public BaseResponse<PostLoginRes> googleOauth2(@RequestHeader(CustomHttpHeaders.GOOGLE_ID) String idTokenString) {

        try {
            GoogleIdToken idToken = googleJwtUtil.CheckGoogleIdTokenVerifier(idTokenString);

            if (idToken == null)
                throw new BaseException(BaseErrorCode.GOOGLE_OAUTH_EXPIRE);

            SocialAccountUserInfo data = oAuthService.getGoogleUserInfo(idToken);

            return new BaseResponse<>(authService.loginWithSocialAccount(data, AccountType.GOOGLE));

        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception);
        }
    }
}