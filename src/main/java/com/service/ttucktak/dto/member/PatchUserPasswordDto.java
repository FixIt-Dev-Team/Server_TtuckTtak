package com.service.ttucktak.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchUserPasswordDto {

    @Schema(name = "isSuccess", example = "true", description = "")
    boolean isSuccess;

}
