package com.service.ttucktak.dto.solution;

import com.service.ttucktak.entity.SolutionEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionPossibleDto {

    @Schema(name = "possible_idx", example = "UUID String", description = "UUID String")
    private UUID possibleIdx;

    @Schema(name = "entry_idx", example = "Long Type", description = "엔트리 Idx")
    private Long entryIdx;

    @Schema(name = "possible_name", example = "예상 원인명", description = "예상 원인명")
    private String possibleName;

    @Builder
    public SolutionPossibleDto(UUID possibleIdx, Long entryIdx, String possibleName) {
        this.possibleIdx = possibleIdx;
        this.entryIdx = entryIdx;
        this.possibleName = possibleName;
    }
}
