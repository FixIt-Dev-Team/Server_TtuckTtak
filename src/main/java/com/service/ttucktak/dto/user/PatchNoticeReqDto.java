package com.service.ttucktak.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버 알람설정 + 야간 알람설정 리퀘스트 DTO
 * */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchNoticeReqDto {
    @Schema(name = "memberIdx", example = "member UUID", requiredProperties =  "true", description = "변경 대상 멤버 인덱스")
    private String memberIdx;

    @Schema(name = "targetStatus", example = "true", requiredProperties =  "true", description = "변경 해야 할 상태 (알람 키기: true)")
    private boolean targetStatus;
}
