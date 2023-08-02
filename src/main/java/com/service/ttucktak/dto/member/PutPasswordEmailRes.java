package com.service.ttucktak.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutPasswordEmailRes {
    @Schema(name = "sendSuccess", example = "true", description = "전송 성공 여부")
    private boolean sendSuccess;
}
