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

    @Schema(name = "entry_idx", example = "Long Type", description = "엔트리 Idx")
    private Long entryIdx;

    @Schema(name = "survey_idx", example = "Long Type", description = "서베이 Idx")
    private Long surveyIdx;

    @Schema(name = "res_pattern", example = "Long Type", description = "응답 패턴")
    private Long resPattern;

    @Schema(name = "issue_type", example = "Long Type", description = "이슈 타입")
    private Long issueType;

    @Schema(name = "problem_name", example = "String Name", description = "문제 명칭")
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
