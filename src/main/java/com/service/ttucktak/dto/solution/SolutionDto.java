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
public class SolutionDto {

    @Schema(name = "solutionIdx", example = "UUID 문자열", description = "솔루션 UUID 스트링")
    private UUID solutionIdx;

    @Schema(name = "issueYype", example = "이슈 타입 (Long)", description = "솔루션 이슈 타입")
    private Long issueType;

    @Schema(name = "descHeader", example = "솔루션 제목", description = "솔루션 제목")
    private String descHeader;

    @Schema(name = "level", example = "솔루션 차수(Integer)", description = "솔루션 차수")
    private Integer level;

    @Builder
    public SolutionDto(UUID solutionIdx, Long issueType, String descHeader, Integer level) {
        this.solutionIdx = solutionIdx;
        this.issueType = issueType;
        this.descHeader = descHeader;
        this.level = level;
    }
}
