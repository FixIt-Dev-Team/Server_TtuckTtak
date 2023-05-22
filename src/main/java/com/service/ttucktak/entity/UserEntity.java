package com.service.ttucktak.entity;

import com.service.ttucktak.base.Role;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "users")
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity {


    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID userIdx;

    @Column(name = "userID", length = 45, nullable = false)
    private String userID; // 유저 아이디
    @Column(name = "userPW", length = 500, nullable = false)
    private String userPW; // 유저 비밀번호
    @Column(name = "userName", length = 45, nullable = false)
    private String userName; // 유저 이름
    @Column(name = "email", length = 100, nullable = false)
    private String email; // 유저 이메일

    @Temporal(TemporalType.DATE) // MySQL에 맞게 타입지정
    @Column(name = "birthday", nullable = false)
    private Date birthday; // 유저 생일

    @ColumnDefault("false") // 기본값 지정
    @Column(name = "validation", nullable = false)
    private Boolean validation; // 유저 인증 여부

    @ColumnDefault("0")
    @Column(name = "accountType", nullable = false)
    private Integer accountType; // 계정 타입

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "createdAt")// 기본값 지정
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Row 생성 시점

    @Column(name = "updatedAt")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Row 업데이트 시점

    @Column(name = "status")
    private Boolean status = true; // Row 유효 상태

    @Builder
    public UserEntity(String userID, String userPW, String userName, String email, Date birthday, Boolean validation, Integer accountType, Role role) {
        this.userID = userID;
        this.userPW = userPW;
        this.userName = userName;
        this.email = email;
        this.birthday = birthday;
        this.validation = validation;
        this.accountType = accountType;
        this.role = role;
    }

    public UserEntity update(String userName, String userEmail, String birthday) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(birthday);

        this.userID = userEmail;
        this.email = userEmail;
        this.userName = userName;
        this.birthday = date;

        return this;
    }
}
