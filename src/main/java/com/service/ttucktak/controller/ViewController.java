package com.service.ttucktak.controller;

import com.service.ttucktak.File.FileService;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.member.UserDataDto;
import com.service.ttucktak.service.MemberService;
import com.service.ttucktak.utils.S3Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private S3Util s3Util;

    @Autowired
    public ViewController(MemberService memberService, FileService fileService, S3Util s3Util){
        this.memberService = memberService;
        this.fileService = fileService;
        this.s3Util = s3Util;
    }

    /*@GetMapping("/main")
    public BaseResponse<>{
        소올직히 지금 유저 히스토리 DB 준비가 안되서 좀 기다려야 것음.
    }*/

    @Operation(summary = "Setting VIEW API", description = "Setting View 유저정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "userIdx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database result NotFound",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/setting")
    public BaseResponse<UserDataDto> settingView(@RequestParam("userIdx") String userIdx,@RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) throws BaseException{

        UUID userId;

        try{
            userId = UUID.fromString(userIdx);
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(memberService.loadUserByUUID(userId));

    }

    @Operation(summary = "Setting VIEW Update API", description = "Setting View 유저 정보 업데이트 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "userIdx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database result NotFound",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "멤버 데이터 처리중 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/setting/update")
    public BaseResponse<PostUserDataResDto> updateUserdata(@RequestPart(value = "ReqDto") PostUserDataReqDto reqDto,
                                                           @RequestPart(value = "files", required = false) MultipartFile file,
                                                           @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) throws BaseException{

        if(file != null){

            String userImgUrl;

            try{

                userImgUrl = s3Util.upload(file,"S3UserProfile_develop");

            }catch (Exception exception){
                log.error("S3 이미지 파일 저장중 문제 발생 : " + exception.getMessage());
                throw new BaseException(BaseErrorCode.UNEXPECTED_ERROR);
            }

            reqDto.setImgUpdate(userImgUrl);

            // 차후 이미지 DB 준비 마무리 되면 그때 추가 작업.
        }

        UUID userIdx;

        try{
            userIdx = UUID.fromString(reqDto.getUserIdx());
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(memberService.updateUserByUUID(userIdx,reqDto));

    }


}
