package com.service.ttucktak.controller;

import com.service.ttucktak.File.FileService;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.member.UserDataDto;
import com.service.ttucktak.dto.member.UserNoticeDto;
import com.service.ttucktak.service.MemberService;
import com.service.ttucktak.utils.S3Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/views")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
@Slf4j
@Tag(name = "View API")
public class ViewController {

    private MemberService memberService;

    private FileService fileService;

    @Autowired
    public ViewController(MemberService memberService, FileService fileService, S3Util s3Util){
        this.memberService = memberService;
        this.fileService = fileService;
    }

    @Operation(summary = "Setting VIEW API", description = "Setting View 유저정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "userIdx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database result NotFound",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/setting")
    public BaseResponse<UserDataDto> settingView(@RequestParam("memberIdx") String memberIdx,@RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) throws BaseException{

        UUID memberId;

        try{
            memberId = UUID.fromString(memberIdx);
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(memberService.loadUserByUUID(memberId));

    }

    @Operation(summary = "Setting VIEW Push Notice API", description = "Setting View push notice 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "userIdx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database result NotFound",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/setting/push")
    public BaseResponse<UserNoticeDto> noticeView(@RequestParam("memberIdx") String memberIdx, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) throws BaseException{

        UUID memberId;

        try{
            memberId = UUID.fromString(memberIdx);
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(memberService.loadUserPushByUUID(memberId));

    }




}
