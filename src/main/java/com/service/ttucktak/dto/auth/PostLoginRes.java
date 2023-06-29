package com.service.ttucktak.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginRes {
    private String userIdx;
    private TokensDto tokenInfo;
}
