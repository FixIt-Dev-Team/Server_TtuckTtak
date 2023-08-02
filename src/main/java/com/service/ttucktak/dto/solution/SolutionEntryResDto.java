package com.service.ttucktak.dto.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionEntryResDto {

    @Schema(name = "entry_idx", example = "솔루션 엔트리 Idx (Long)", description = "솔루션 엔트리 Idx")
    private Long entryIdx;

    @Schema(name = "level", example = "솔루션 차수 Integer)", description = "솔루션 차수")
    private Integer level;

    @Schema(name = "problem_name", example = "문제 명칭", description = "문제 명칭")
    private String problemName;

    @Schema(name = "Solution List", description = "솔루션 리스트")
    private List<SolutionDto> solutionDtos;

    @Schema(name = "SolutionPossible List", description = "예상 원인 리스트")
    private List<SolutionPossibleDto> solutionPossibleDtos;

    @Schema(name = "SolutionBypass List", description = "바이패스 리스트")
    private List<SolutionBypassDto> solutionBypassDtos;

    @Builder
    public SolutionEntryResDto(Long entryIdx, Integer level, String problemName,
                               List<SolutionDto> solutionDtos,
                               List<SolutionPossibleDto> solutionPossibleDtos,
                               List<SolutionBypassDto> solutionBypassDtos) {
        this.entryIdx = entryIdx;
        this.level = level;
        this.problemName = problemName;
        this.solutionDtos = solutionDtos;
        this.solutionPossibleDtos = solutionPossibleDtos;
        this.solutionBypassDtos = solutionBypassDtos;
    }
}
