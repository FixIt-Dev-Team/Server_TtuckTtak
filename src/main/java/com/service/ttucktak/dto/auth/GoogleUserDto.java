package com.service.ttucktak.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;


@Getter
@Builder
@ToString
public class GoogleUserDto {
    private final String userName;
    private final String userEmail;
    private final String imgURL;
    private Date birthday;
    private static int accountType;
}
