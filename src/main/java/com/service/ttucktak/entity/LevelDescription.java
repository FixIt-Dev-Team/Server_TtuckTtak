package com.service.ttucktak.entity;

import com.service.ttucktak.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "level_desc")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelDescription extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "desc_idx", nullable = false)
    private UUID descIdx;

    //TODO 연관관계 매핑
    @JoinColumn(name = "entry_idx", referencedColumnName = "entry_idx", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private SolutionEntry entryIdx;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "description", nullable = false)
    private String description;

    @Builder
    public LevelDescription(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, UUID descIdx, SolutionEntry entryIdx, Integer level, String description) {
        super(createdAt, updatedAt, status);
        this.descIdx = descIdx;
        this.entryIdx = entryIdx;
        this.level = level;
        this.description = description;
    }
}
