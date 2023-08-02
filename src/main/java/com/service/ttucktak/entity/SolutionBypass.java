package com.service.ttucktak.entity;

import com.service.ttucktak.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "soultion_bypass")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionBypass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bypass_idx", nullable = false)
    private UUID bypassIdx;

    @Column(name = "start_entry_idx", nullable = false)
    private Long startEntryIdx;

    @Column(name = "target_entry_idx", nullable = false)
    private Long targetEntryIdx;

    @Column(name = "target_entry_name", nullable = false)
    private String targetEntryName;

    @Builder
    public SolutionBypass(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, UUID bypassIdx, Long startEntryIdx, Long targetEntryIdx, String targetEntryName) {
        super(createdAt, updatedAt, status);
        this.bypassIdx = bypassIdx;
        this.startEntryIdx = startEntryIdx;
        this.targetEntryIdx = targetEntryIdx;
        this.targetEntryName = targetEntryName;
    }
}
