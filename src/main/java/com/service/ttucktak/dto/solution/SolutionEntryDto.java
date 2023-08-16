package com.service.ttucktak.dto.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionEntryDto {

    @Schema(name = "entryIdx", example = "솔루션 엔트리 Idx (Long)", description = "솔루션 엔트리 Idx")
    private Long entryIdx;

    @Schema(name = "surveyIdx", example = "설문 Idx (Long)", description = "안드로이드 설문 Idx")
    private Long surveyIdx;

    @Schema(name = "resPattern", example = "안드로이드 설문 응답 패턴(Long)", description = "안드로이드 설문 응답 패턴")
    private Long resPattern;

    @Schema(name = "issueYype", example = "솔루션 이슈 타입 (Long)", description = "이슈 타입")
    private Long issueType;

    @Schema(name = "problemName", example = "문제 이름", description = "문제 이름")
    private String problemName;

    @Builder
    public SolutionEntryDto(Long entryIdx, Long surveyIdx, Long resPattern, Long issueType, String problemName) {
        this.entryIdx = entryIdx;
        this.surveyIdx = surveyIdx;
        this.resPattern = resPattern;
        this.issueType = issueType;
        this.problemName = problemName;
    }

}
