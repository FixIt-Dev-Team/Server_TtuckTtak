package com.service.ttucktak.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "Users")
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

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdAt", nullable = false)// 기본값 지정
    @Temporal(TemporalType.TIMESTAMP) // MySQL에 맞게 타입지정
    private Date createdAt; // Row 생성 시점

    @ColumnDefault("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP") // 기본값 지정
    @Column(name = "updatedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // MySQL에 맞게 타입지정
    private Date updatedAt; // Row 업데이트 시점

    @ColumnDefault("true")
    @Column(name = "status", nullable = false)
    private Boolean status; // Row 유효 상태
}
