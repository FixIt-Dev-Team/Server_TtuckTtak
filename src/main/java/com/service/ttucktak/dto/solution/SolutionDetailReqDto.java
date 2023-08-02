package com.service.ttucktak.dto.solution;

import com.service.ttucktak.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionDetailReqDto {

    @Schema(name = "sol_idx", example = "UUID String", description = "솔루션 Idx")
    private String solutionIdx;

    @Builder
    public SolutionDetailReqDto(String solutionIdx) {
        this.solutionIdx = solutionIdx;
    }
}
