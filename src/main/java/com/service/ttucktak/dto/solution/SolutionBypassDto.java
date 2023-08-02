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

    @Schema(name = "bypassIdx", example = "UUID 문자열", description = "Bypass UUID")
    private UUID bypassIdx;

    @Schema(name = "startEntryIdx", example = "Long Type Idx", description = "바이패스 출발 엔트리 Idx (현재 엔트리)")
    private Long startEntryIdx;

    @Schema(name = "targetEntryIdx", example = "Long Type Idx", description = "바이패스 도착 엔트리Idx (바이패스할 엔트리)")
    private Long targetEntryIdx;

    @Schema(name = "targetEntryName", example = "바이패스할 엔트리 네임", description = "바이패스 도착 엔트리 네임")
    private String targetEntryName;

    @Builder
    public SolutionBypassDto(UUID bypassIdx, Long startEntryIdx, Long targetEntryIdx, String targetEntryName) {
        this.bypassIdx = bypassIdx;
        this.startEntryIdx = startEntryIdx;
        this.targetEntryIdx = targetEntryIdx;
        this.targetEntryName = targetEntryName;
    }
}
