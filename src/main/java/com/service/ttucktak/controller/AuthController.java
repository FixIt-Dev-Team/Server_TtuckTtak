package com.service.ttucktak.controller;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.oAuth.OAuthService;
import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.service.EmailService;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
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

    /**
     * 회원가입 + 로그인 처리
     */
    @Operation(summary = "회원가입", description = "서비스에 회원가입 및 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "이메일 형식에 맞지 않습니다. | 닉네임 형식에 맞지 않습니다. | 비밀번호 형식에 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 아이디입니다. | 이미 존재하는 닉네임입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/signup")
    @Transactional
    public BaseResponse<PostLoginRes> signUp(@RequestBody @Valid PostSignUpReqDto data) throws BaseException{
        // 회원가입한 사용자의 정보를 기반으로 로그인 처리
        Member newMember = authService.signUp(data);
        PostLoginRes result = authService.login(newMember, data.getUserPw());

        return new BaseResponse<>(result);
    }

    /**
     * 로그인
     */
    @Operation(summary = "로그인", description = "사용자의 id와 pw를 이용해 서비스 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "이메일 혹은 비밀번호가 틀렸습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> userLogin(@RequestBody PostLoginReq req) throws BaseException{
        PostLoginRes result = authService.login(req.getUserId(), req.getUserPw());
        return new BaseResponse<>(result);
    }

    /**
     * 토큰 갱신
     * */
    @Operation(summary = "AccessToken 갱신", description = "AccessToken 갱신을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "리프래시 토큰 만료 재 로그인 바랍니다. | 유저가 존재하지 않습니다. | 리프레시 토큰 불일치 재 로그인 바랍니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/token/refresh")
    public BaseResponse<TokensDto> refreshAccessToken(@RequestBody PostRefreshTokenDto req) throws Exception {
        return new BaseResponse<>(authService.refreshAccessToken(req));
    }

    @Operation(summary = "리프레쉬 토큰 비활성 (서비스 내부 로그아웃)", description = "사용자의 계정 로그아웃 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Idx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/logout")
    public BaseResponse<PostLogoutRes> userLogout(@RequestBody PostLogoutReq req) throws BaseException{
        UUID userIdx = UUID.fromString(req.getUserIdx());
        return new BaseResponse<>(authService.logout(userIdx));
    }

    /**
     * 이메일 인증
     */
    @Operation(summary = "이메일 인증", description = "유효한 이메일 인지 인증 코드를 보내 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/email-confirm")
    public BaseResponse<PostEmailConfirmResDto> emailConfirm(@Email @RequestParam String to) throws BaseException{
        //이메일 존재 여부 체크
        if(authService.isEmailExist(to)) throw new BaseException(BaseErrorCode.ALREADY_EXIST_EMAIL);

        return new BaseResponse<>(emailService.sendSimpleMessage(to));


    }

    /**
     * 카카오 계정을 통해 로그인
     */
    @Operation(summary = "카카오 계정을 통해 로그인", description = "사용자의 카카오 인가 코드를 사용하여 서비스에 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "카카오 이메일 동의가 필요합니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 사용중인 이메일입니다. 이전에 사용하시던 계정으로 사용해 주세요.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다. | 카카오 로그인 중 오류발생 서버에 문의",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @GetMapping("/oauth2/kakao")
    public BaseResponse<PostLoginRes> kakaoOauth2(@RequestParam("code") String authCode) throws BaseException{
        String authToken = oAuthService.getKakaoAccessToken(authCode);

        SocialAccountUserInfo data = oAuthService.getKakaoUserInfo(authToken);

        return new BaseResponse<>(authService.loginWithSocialAccount(data, AccountType.KAKAO));
        //유저 존재 여부에 따라 회원가입 처리후 로그인 처리

    }

    /**
     * 구글 계정을 통해 서비스 로그인
     */
    @Operation(summary = "구글 계정을 통해 로그인", description = "사용자의 구글 id token을 사용하여 서비스에 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "구글 로그인 중 ID 토큰 검증 실패. 오류 발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 사용중인 이메일입니다. 이전에 사용하시던 계정으로 사용해 주세요.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error | 예상치 못한 에러가 발생하였습니다. | 구글 로그인 중 GoogleIDToken Payload 과정에서 오류 발생. 서버에 문의. | 구글 JWT 토큰 인증 중 구글 시큐리티 문제 발생. | 구글 JWT 토큰 인증 중 IO 문제 발생.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/oauth2/google")
    public BaseResponse<PostLoginRes> googleOauth2(@RequestHeader(CustomHttpHeaders.GOOGLE_ID) String idTokenString) throws BaseException{

        GoogleIdToken idToken = googleJwtUtil.CheckGoogleIdTokenVerifier(idTokenString);

        if (idToken == null)
            throw new BaseException(BaseErrorCode.GOOGLE_OAUTH_EXPIRE);

        SocialAccountUserInfo data = oAuthService.getGoogleUserInfo(idToken);
        return new BaseResponse<>(authService.loginWithSocialAccount(data, AccountType.GOOGLE));
    }
}