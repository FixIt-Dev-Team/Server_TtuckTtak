package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.ttucktak.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(description = "자체 회원가입 Request DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class PostSigninReqDto {

    @ApiModelProperty(value = "유저 아이디", required = true, example = "Rookie")
    private String userID;

    @ApiModelProperty(value = "유저 비밀번호", required = true, example = "12345678")
    private String userPW;

    @ApiModelProperty(value = "유저 이름", required = true, example = "Rooike")
    private String userName;

    @ApiModelProperty(value = "유저 이메일", required = true, example = "vc54@naver.com")
    private String email;

    @ApiModelProperty(value = "유저 생일", required = true, example = "1998-11-14")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(value = "계정 타입", required = true, example = "0")
    private int accountType;

    public UserEntity toEntity(boolean validation){
        return UserEntity.builder()
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
