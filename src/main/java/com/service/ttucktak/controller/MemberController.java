package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.auth.PatchPasswordLostReq;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.member.*;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.auth.PutPasswordUpdateDto;
import com.service.ttucktak.service.EmailService;
import com.service.ttucktak.entity.annotation.Nickname;
import com.service.ttucktak.service.MemberService;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
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
import org.springframework.ui.Model;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Tag(name = "Member API")
@Validated
@RestController
@RequestMapping("/api/members")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
public class MemberController {
    private final JwtUtil jwtUtil;

    private final RegexUtil regexUtil;

    private final MemberService memberService;

    private final EmailService emailService;

    private S3Util s3Util;

    @Autowired
    public MemberController(JwtUtil jwtUtil, RegexUtil regexUtil, MemberService memberService, EmailService emailService,S3Util s3Util){
        this.jwtUtil = jwtUtil;
        this.regexUtil = regexUtil;
        this.memberService = memberService;
        this.emailService = emailService;
        this.s3Util = s3Util;
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
    public BaseResponse<GetNicknameAvailableResDto> checkNicknameAvailability(@Nickname @RequestParam("nickname") String nickname) {
        try {
            return new BaseResponse<>(memberService.nicknameAvailable(nickname));

        } catch (BaseException e) {
            log.error(e.getMessage());
            return new BaseResponse<>(e);
        }
    }

    /**
     * 닉네임 변경 API
     */
    @Operation(summary = "닉네임 변경", description = "닉네임 변경을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/nickname")
    public BaseResponse<PatchNicknameResDto> patchNickname(@RequestBody PatchNicknameReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) {
        try {
            return new BaseResponse<>(memberService.patchNickname(req));

        } catch (BaseException exception) {
            return new BaseResponse<>(exception);
        }
    }

    @Operation(summary = "닉네임 및 프로필 변경 API", description = "유저 정보 업데이트 API(닉네임 및 프로필)")
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
    @PatchMapping(value = "/updateprofile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    /**
     * Push 알림 설정 API
     */
    @Operation(summary = "Push 알림 설정", description = "Push 알림 설정을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "현 설정과 동일한 값입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/push")
    public BaseResponse<PatchNoticeResDto> patchPushNotice(@RequestBody PatchNoticeReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) {
        try {
            return new BaseResponse<>(memberService.patchPushNotice(req));

        } catch (BaseException exception) {
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 야간 Push 알림 설정 API
     */
    @Operation(summary = "야간 Push 알림 설정", description = "야간 Push 알림 설정을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "현 설정과 동일한 값입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/push/night")
    public BaseResponse<PatchNoticeResDto> patchNightNotice(@RequestBody PatchNoticeReqDto req, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) {
        try {
            return new BaseResponse<>(memberService.patchNightNotice(req));

        } catch (BaseException exception) {
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 유저 패스워드 업데이트 설정 API
     */
    @Operation(summary = "패스워드 업데이트 설정", description = "사용자 패스워드 업데이트를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "userIdx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database result NotFound",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database Error",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "패스워드 업데이트 처리중 예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping("/password")
    public BaseResponse<PostUserDataResDto> updateUserdata(@RequestBody PutPasswordUpdateDto reqDto,@RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt) throws BaseException{
        UUID userIdx;

        try{
            userIdx = UUID.fromString(reqDto.getMemberIdx());
        }catch(Exception exception){
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        if (!RegexUtil.isValidPwFormat(reqDto.getNewPw())) throw new BaseException(BaseErrorCode.INVALID_PW_FORMAT);

        return new BaseResponse<>(memberService.updateUserPasswordByUUID(userIdx, reqDto));
    }

    @Operation(deprecated = true)
    @PatchMapping("/password/lost")
    public Boolean updatePasswordLost(@RequestBody PatchPasswordLostReq reqDto) throws Exception{

        log.error(reqDto.getEmail());
        if (!RegexUtil.isValidPwFormat(reqDto.getNewPw())) throw new BaseException(BaseErrorCode.INVALID_PW_FORMAT);

        try{
            return memberService.updateUserPasswordByEmail(reqDto);
        }catch (BaseException exception){
            log.error(exception.getMessage());
            throw exception;
        }

    }

    /**
     * 타임리프 비밀번호 변경 페이지 이메일 전송 API
     * */
    @Operation(summary = "비밀번호 변경 이메일 보내기", description = "비밀번호 변경 이메일을 보내기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "올바르지 않은 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 이메일을 사용하는 유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PutMapping ("/password/email")
    public BaseResponse<PutPasswordEmailRes> passwordPage(Model model, @RequestParam("target-email") String to) throws BaseException{
        if(!RegexUtil.isValidEmailFormat(to)){
            throw new BaseException(BaseErrorCode.INVALID_EMAIL);
        }

        if(!memberService.isEmailExist(to)) throw new BaseException(BaseErrorCode.MEMBER_NOT_EXIST);

        String addr = "https://ttukttak.store/page/leafs/password/page?email=" + to;
        return new BaseResponse<>(new PutPasswordEmailRes(emailService.sendPasswordModify(to, addr)));

    }
}
