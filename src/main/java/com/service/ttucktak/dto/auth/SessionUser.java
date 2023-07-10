package com.service.ttucktak.dto.auth;

import com.service.ttucktak.entity.Member;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class SessionUser implements Serializable {
    private final String userName;
    private final String userEmail;

    public SessionUser(Member users) {
        this.userName = users.getNickname();
        this.userEmail = users.getUserId();
    }
}
