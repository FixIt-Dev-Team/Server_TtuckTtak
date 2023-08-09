package com.service.ttucktak.dto.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionEntryReqDto {

    @Schema(name = "surveyIdx", example = "엔트리 인덱스(정수)", requiredProperties = "true", description = "설문 Idx")
    private Long surveyIdx;

    @Schema(name = "resPattern", example = "응답패턴(1과 0으로 이루어진 숫자로)", requiredProperties = "true", description = "응답 패턴")
    private Long resPattern;

    @Schema(name = "level", example = "차수 숫자(정수)", requiredProperties = "true", description = "차수")
    private Integer level;

    @Builder
    public SolutionEntryReqDto(Long surveyIdx, Long resPattern, Integer level) {
        this.surveyIdx = surveyIdx;
        this.resPattern = resPattern;
        this.level = level;
    }
}
