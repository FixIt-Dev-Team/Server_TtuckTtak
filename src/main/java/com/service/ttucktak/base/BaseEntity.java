package com.service.ttucktak.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * 모든 엔티티에 공통으로 들어가는 요소
 * @author LEE JIHO
 * @usage 각 엔티티데 BaseEntity를 extends 받아 사용
 * */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "createdAt", nullable = false, updatable = false)// 기본값 지정
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Row 생성 시점

    @Column(name = "updatedAt", nullable = false)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Row 업데이트 시점

    @Column(name = "status", nullable = false)
    private Boolean status = true; // Row 유효 상태
}
