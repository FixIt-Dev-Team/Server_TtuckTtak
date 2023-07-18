package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.member.PatchNicknameReqDto;
import com.service.ttucktak.dto.member.PatchNicknameResDto;
import com.service.ttucktak.dto.member.PatchNoticeReqDto;
import com.service.ttucktak.dto.member.PatchNoticeResDto;
import com.service.ttucktak.service.MemberService;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API")
@RestController
@RequestMapping("/api/members")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
public class MemberController {
    private final JwtUtil jwtUtil;

    private final RegexUtil regexUtil;

    private final MemberService memberService;

    @Autowired
    public MemberController(JwtUtil jwtUtil, RegexUtil regexUtil, MemberService memberService){
        this.jwtUtil = jwtUtil;
        this.regexUtil = regexUtil;
        this.memberService = memberService;
    }

    /**
     * 닉네임 변경 API
     * */
    @Operation(summary = "닉네임 변경", description = "닉네임 변경을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/nickname")
    public BaseResponse<PatchNicknameResDto> patchNickname(@RequestBody PatchNicknameReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) {

        try{
            return new BaseResponse<>(memberService.patchNickname(req));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }

    /**
     * Push 알림 설정 API
     * */
    @Operation(summary = "Push 알림 설정", description = "Push 알림 설정을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "현 설정과 동일한 값입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/push")
    public BaseResponse<PatchNoticeResDto> patchPushNotice(@RequestBody PatchNoticeReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt){

        try{
            return new BaseResponse<>(memberService.patchPushNotice(req));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 야간 Push 알림 설정 API
     * */
    @Operation(summary = "야간 Push 알림 설정", description = "야간 Push 알림 설정을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "현 설정과 동일한 값입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/push/night")
    public BaseResponse<PatchNoticeResDto> patchNightNotice(@RequestBody PatchNoticeReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt){
        try {
            return new BaseResponse<>(memberService.patchNightNotice(req));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }
}
