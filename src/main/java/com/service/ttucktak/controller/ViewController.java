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
    //Multipart form data임을 보여주어야 해서 어노테이션에 속성 추가
    @PatchMapping(value = "/setting/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PostUserDataResDto> updateUserdata(@RequestPart(value = "ReqDto") @Parameter(description = "Try It Out 클릭하시면 데이터가 나와요") PostUserDataReqDto reqDto,
                                                           @RequestPart(value = "file", required = false) @Parameter(description = "프로필 이미지") MultipartFile file,
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

        UUID memberIdx;

        try{
            memberIdx = UUID.fromString(reqDto.getMemberIdx());
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(memberService.updateUserByUUID(memberIdx,reqDto));

    }


}
