package com.service.ttucktak.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Entity(name = "Profile")
@NoArgsConstructor
public class ProfileEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID profileIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @Column(name = "iconType", nullable = false)
    private Integer iconType;

    @ColumnDefault("'프로필 설명을 입력해 주세요'")
    @Column(name = "profileDesc", length = 500, nullable = false)
    private String profileDesc;

    @Column(name = "nickName", length = 45, nullable = false)
    private String nickName;

    @ColumnDefault("CURRENT_TIMESTAMP") // 기본값 지정
    @Temporal(TemporalType.TIMESTAMP) // MySQL에 맞게 타입지정
    @Column(name = "createdAt", nullable = false)
    private Date createdAt; // Row 생성 시점

    @ColumnDefault("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP") // 기본값 지정
    @Temporal(TemporalType.TIMESTAMP) // MySQL에 맞게 타입지정
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt; // Row 업데이트 시점

    @ColumnDefault("true")
    @Column(name = "status", nullable = false)
    private Boolean status; // Row 유효 상태
}
