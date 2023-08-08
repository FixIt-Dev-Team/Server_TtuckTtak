package com.service.ttucktak.dto.member;

import com.service.ttucktak.base.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserNoticeDto {
    @Schema(name = "push_approve", example = "true", requiredProperties = "true", description = "push 알림 동의")
    private boolean pushStatus;
    @Schema(name = "night_approve", example = "true", requiredProperties = "true", description = "야간 push 알림 동의")
    private boolean nightPushStatus;

    @Builder
    public UserNoticeDto(boolean pushStatus,boolean nightPushStatus){
        this.pushStatus = pushStatus;
        this.nightPushStatus = nightPushStatus;
    }
}
