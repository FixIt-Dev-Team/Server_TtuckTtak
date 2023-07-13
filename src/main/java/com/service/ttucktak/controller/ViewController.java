package com.service.ttucktak.controller;

import com.service.ttucktak.File.FileService;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomUserDetailService;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.user.UserDataDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/views")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
@Slf4j
@Tag(name = "회원 인증 API")
public class ViewController {

    private CustomUserDetailService userDetailService;

    private FileService fileService;

    @Autowired
    public ViewController(CustomUserDetailService userDetailService, FileService fileService){
        this.userDetailService = userDetailService;
        this.fileService = fileService;
    }

    /*@GetMapping("/main")
    public BaseResponse<>{
        이녀석도 소올직히 지금 유저 히스토리 DB 준비가 안되서 좀 기다려야 것음.
    }*/

    @GetMapping("/setting")
    public BaseResponse<UserDataDto> settingView(@RequestParam("userIdx") String userIdx) throws BaseException{

        UUID userId;

        try{
            userId = UUID.fromString(userIdx);
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(userDetailService.loadUserByUUID(userId));

    }

    @PostMapping("/setting/update")
    public BaseResponse<PostUserDataResDto> updateUserdata(@RequestPart(value = "ReqDto") PostUserDataReqDto reqDto,
                                                           @RequestPart(value = "files", required = false) MultipartFile file) throws BaseException{

        if(file != null){

            try{
                fileService.fileUpload(file);
            }catch (Exception exception){
                log.error("파일 저장중 문제 발생 : " + exception.getMessage());
                throw new BaseException(BaseErrorCode.UNEXPECTED_ERROR);
            }
            // 차후 이미지 DB 준비 마무리 되면 그때 추가 작업.
        }

        UUID userIdx;

        try{
            userIdx = UUID.fromString(reqDto.getUserIdx());
        }catch(Exception exception){
            log.error("UUID 변환중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        return new BaseResponse<>(userDetailService.updateUserByUUID(userIdx,reqDto));

    }


}
