package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.ttucktak.entity.Users;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSignUpReqDto {

    private String userID;

    private String userPW;

    private String userName;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private int accountType;

    public Users toEntity(boolean validation){
        return Users.builder()
                .userID(userID)
                .userPW(userPW)
                .userName(userName)
                .email(email)
                .birthday(birthday)
                .validation(validation)
                .accountType(accountType)
                .build();
    }
}
