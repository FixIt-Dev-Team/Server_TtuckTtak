package com.service.ttucktak.dto.solution;

import com.service.ttucktak.base.AccountType;
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
public class SolutionBypassDto {

    @Schema(name = "bypassIdx", example = "UUID String", description = "Bypass UUID")
    private UUID bypassIdx;

    @Schema(name = "startEntryIdx", example = "Long Type", description = "접근 엔트리Idx")
    private Long startEntryIdx;

    @Schema(name = "targetEntryIdx", example = "Long Type", description = "타겟 엔트리Idx")
    private Long targetEntryIdx;

    @Schema(name = "targetEntryName", example = "Long Type", description = "타겟 엔트리 네임")
    private Long targetEntryName;

    @Builder
    public SolutionBypassDto(UUID bypassIdx, Long startEntryIdx, Long targetEntryIdx, Long targetEntryName) {
        this.bypassIdx = bypassIdx;
        this.startEntryIdx = startEntryIdx;
        this.targetEntryIdx = targetEntryIdx;
        this.targetEntryName = targetEntryName;
    }
}
