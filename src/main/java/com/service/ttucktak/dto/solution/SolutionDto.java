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

    @Schema(name = "sol_idx", example = "UUID String", description = "UUID 스트링")
    private UUID solutionIdx;

    @Schema(name = "issue_type", example = "Long Type", description = "이슈 타입")
    private Long issueType;

    @Schema(name = "desc_header", example = "String Header", description = "솔루션 헤더")
    private String descHeader;

    @Schema(name = "level", example = "Integer Type", description = "차수")
    private Integer level;

    @Builder
    public SolutionDto(UUID solutionIdx, Long issueType, String descHeader, Integer level) {
        this.solutionIdx = solutionIdx;
        this.issueType = issueType;
        this.descHeader = descHeader;
        this.level = level;
    }
}
