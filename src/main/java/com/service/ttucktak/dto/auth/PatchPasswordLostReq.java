package com.service.ttucktak.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor
public class PatchPasswordLostReq {

    private String email;

    private String newPw;
}
