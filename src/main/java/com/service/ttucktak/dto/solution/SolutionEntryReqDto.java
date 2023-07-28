package com.service.ttucktak.dto.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionEntryReqDto {

    @Schema(name = "entry_idx", example = "Long Type", requiredProperties = "true", description = "엔트리 Idx")
    private Long entryIdx;

    @Schema(name = "survey_idx", example = "Long Type", requiredProperties = "true", description = "설문 Idx")
    private Long surveyIdx;

    @Schema(name = "res_pattern", example = "Long Type", requiredProperties = "true", description = "응답 패턴")
    private Long resPattern;

    @Schema(name = "level", example = "Integer Type", requiredProperties = "true", description = "차수")
    private Integer level;

    @Builder
    public SolutionEntryReqDto(Long entryIdx, Long surveyIdx, Long resPattern, Integer level) {
        this.entryIdx = entryIdx;
        this.surveyIdx = surveyIdx;
        this.resPattern = resPattern;
        this.level = level;
    }
}
