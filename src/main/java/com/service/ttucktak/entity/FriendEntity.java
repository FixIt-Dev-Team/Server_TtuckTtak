package com.service.ttucktak.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "Friend")
@Getter
@NoArgsConstructor
public class FriendEntity {
    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID friendIdx;

    @ManyToOne // Many (친구) One(유저) 한명의 유저는 여러명의 친구를 가질 수 있다.
    @JoinColumn(name = "hostIdx")
    private UserEntity hostIdx; // 친구 소유 인덱슨

    @ManyToOne // Many (친구) One(유저) 한명의 유저는 여러명의 유저에게 친구 대상이 될 수 있다.
    @JoinColumn(name = "targetIdx")
    private UserEntity targetIdx; // 친구 대상 인덱스

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
