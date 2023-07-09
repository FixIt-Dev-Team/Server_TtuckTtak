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

import java.util.Date;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "solution_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionImage extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "sol_img_idx", nullable = false)
    private UUID SolutionImageIdx;

    @JoinColumn(name = "detail_idx", referencedColumnName = "detail_idx", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private SolutionDetail detailIdx;

    @Column(name = "img_url", nullable = false)
    private String imageUrl;

    @Builder
    public SolutionImage(Date createdAt, Date updatedAt, Boolean status, UUID solutionImageIdx, SolutionDetail detailIdx, String imageUrl) {
        super(createdAt, updatedAt, status);
        SolutionImageIdx = solutionImageIdx;
        this.detailIdx = detailIdx;
        this.imageUrl = imageUrl;
    }
}
