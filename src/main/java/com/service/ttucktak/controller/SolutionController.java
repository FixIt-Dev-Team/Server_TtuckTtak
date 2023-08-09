package com.service.ttucktak.controller;

import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.base.BaseResponse;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.solution.SolutionDetailReqDto;
import com.service.ttucktak.dto.solution.SolutionDetailResDto;
import com.service.ttucktak.dto.solution.SolutionEntryReqDto;
import com.service.ttucktak.dto.solution.SolutionEntryResDto;
import com.service.ttucktak.service.SolutionService;
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

import java.util.UUID;

@Tag(name = "Solution API")
@RestController
@RequestMapping("/api/solutions")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
public class SolutionController {

    private final SolutionService solutionService;

    @Autowired
    public SolutionController(SolutionService solutionService){
        this.solutionService = solutionService;
    }

    /**
     * 솔루션 엔트리 조회 API
     * */
    @Operation(summary = "솔루션 엔트리 조회", description = "현 상황 및 설문에 따른 해결법 엔트리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "솔루션이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping ("/entry")
    public BaseResponse<SolutionEntryResDto> findEntry(@RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt, @RequestBody SolutionEntryReqDto req){

        try{
            return new BaseResponse<>(solutionService.findAndThrowSolution(req,null));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 솔루션 디테일 조회 API
     * */
    @Operation(summary = "솔루션 디테일 조회", description = "해결 방법 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Idx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "솔루션 상세정보가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/detail")
    public BaseResponse<SolutionDetailResDto> findDetail(@RequestParam("solutionIdx") String solutionIdx, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt){

        try{
            return new BaseResponse<>(solutionService.loadDetail(solutionIdx));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }

    /**
     * 솔루션 엔트리 조회(IDX) API
     * */
    @Operation(summary = "솔루션 엔트리 조회(IDX)", description = "바이패스를 통한 해결법 엔트리 조회 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Idx 값에 오류 발생",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "솔루션이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러가 발생하였습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/entryIdx")
    public BaseResponse<SolutionEntryResDto> findEntry(@RequestParam("entryIdx") Long entryIdx, @RequestHeader(CustomHttpHeaders.AUTHORIZATION) String jwt){


        try{
            return new BaseResponse<>(solutionService.findAndThrowSolution(null,entryIdx));
        }catch (BaseException exception){
            return new BaseResponse<>(exception);
        }
    }

}
