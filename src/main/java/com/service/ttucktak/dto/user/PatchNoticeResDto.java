package com.service.ttucktak.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchNoticeResDto {

    @Schema(name = "changedStatus", example = "true", description = "변경 이후 상태")
    private boolean changedStatus;
}
