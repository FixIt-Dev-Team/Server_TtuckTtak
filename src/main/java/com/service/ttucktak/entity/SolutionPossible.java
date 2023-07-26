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
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "solution_possible")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionPossible extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "possible_idx")
    private UUID possibleIdx;

    @ManyToOne
    @JoinColumn(name = "entry_idx")
    private SolutionEntry entryIdx;

    @Column(name = "possible_name")
    private String possibleName;

    @Builder
    public SolutionPossible(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, UUID possibleIdx, SolutionEntry entryIdx, String possibleName) {
        super(createdAt, updatedAt, status);
        this.possibleIdx = possibleIdx;
        this.entryIdx = entryIdx;
        this.possibleName = possibleName;
    }
}
