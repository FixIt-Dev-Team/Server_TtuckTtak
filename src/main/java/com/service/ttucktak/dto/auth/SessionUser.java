package com.service.ttucktak.dto.auth;

import com.service.ttucktak.entity.Users;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class SessionUser implements Serializable {
    private final String userName;
    private final String userEmail;
    private final Date birthday;

    public SessionUser(Users users) {
        this.userName = users.getUserName();
        this.userEmail = users.getEmail();
        this.birthday = users.getBirthday();
    }
}
