package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.solution.*;
import com.service.ttucktak.entity.*;
import com.service.ttucktak.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 유저 서비스단 클래스
 * */
@Service
@Slf4j
@Transactional
public class SolutionService {
    private final SolutionRepository solutionRepository;

    private final SolutionPossibleRepository solutionPossibleRepository;

    private final SolutionImageRepository solutionImageRepository;

    private final SolutionEntryRepository solutionEntryRepository;

    private final SolutionDetailRepository solutionDetailRepository;

    private final SolutionBypassRepository solutionBypassRepository;

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     * */
    @Autowired
    public SolutionService(SolutionRepository solutionRepository,
                           SolutionPossibleRepository solutionPossibleRepository,
                           SolutionImageRepository solutionImageRepository,
                           SolutionEntryRepository solutionEntryRepository,
                           SolutionDetailRepository solutionDetailRepository,
                           SolutionBypassRepository solutionBypassRepository) {

        this.solutionRepository = solutionRepository;
        this.solutionPossibleRepository = solutionPossibleRepository;
        this.solutionImageRepository = solutionImageRepository;
        this.solutionEntryRepository = solutionEntryRepository;
        this.solutionDetailRepository = solutionDetailRepository;
        this.solutionBypassRepository = solutionBypassRepository;

    }

    public SolutionEntryResDto findAndThrowSolution(SolutionEntryReqDto req, Long entryIdx) throws BaseException {

        Optional<SolutionEntry> entry;

        if(req !=null){
            entry = solutionEntryRepository.findBySurveyIdxAndResPattern(req.getSurveyIdx(),req.getResPattern());
        }else{
            entry = solutionEntryRepository.findByEntryIdx(entryIdx);
        }

        entry.orElseThrow(()->new BaseException(BaseErrorCode.SOLUTION_NOT_FOUND));

        SolutionEntry solutionEntry = entry.get();

        List<Solution> solutions = solutionRepository.findByIssueTypeAndLevel(solutionEntry.getIssueType(),(req == null) ? 1 : req.getLevel());

        List<SolutionDto> solutionDtos = new ArrayList<SolutionDto>();

        if(solutions.isEmpty()){
            throw new BaseException(BaseErrorCode.SOLUTION_NOT_FOUND);
        }else{
            for (Solution solution : solutions){
                solutionDtos.add(SolutionDto.builder()
                        .solutionIdx(solution.getSolutionIdx())
                        .level(solution.getLevel())
                        .issueType(solution.getIssueType())
                        .descHeader(solution.getDescHeader())
                        .build()
                );
            }
        }

        List<SolutionPossible> solutionPossibles = solutionPossibleRepository.findByEntryIdx_EntryIdx(solutionEntry.getEntryIdx());

        List<SolutionPossibleDto> solutionPossibleDtos = new ArrayList<SolutionPossibleDto>();

        if(solutionPossibles.isEmpty()){
            solutionPossibleDtos = null;
        }else{
            for (SolutionPossible solutionPossible : solutionPossibles){
                solutionPossibleDtos.add(SolutionPossibleDto.builder()
                                .entryIdx(solutionEntry.getEntryIdx())
                                .possibleIdx(solutionPossible.getPossibleIdx())
                                .possibleName(solutionPossible.getPossibleName())
                        .build()
                );
            }
        }

        List<SolutionBypass> solutionBypasses = solutionBypassRepository.findByStartEntryIdx(solutionEntry.getEntryIdx());

        List<SolutionBypassDto> solutionBypassDtos = new ArrayList<>();

        if(solutionBypasses.isEmpty()){
            solutionBypassDtos = null;
        }else{
            for (SolutionBypass solutionBypass : solutionBypasses){
                solutionBypassDtos.add(SolutionBypassDto.builder()
                                .startEntryIdx(solutionBypass.getStartEntryIdx())
                                .targetEntryName(solutionBypass.getTargetEntryName())
                                .bypassIdx(solutionBypass.getBypassIdx())
                                .targetEntryIdx(solutionBypass.getTargetEntryIdx())
                        .build()
                );
            }
        }

        return SolutionEntryResDto.builder()
                .entryIdx(solutionEntry.getEntryIdx())
                .level((req!=null) ? req.getLevel() : 1)
                .problemName(solutionEntry.getProblemName())
                .solutionDtos(solutionDtos)
                .solutionPossibleDtos(solutionPossibleDtos)
                .solutionBypassDtos(solutionBypassDtos)
                .build();
    }

    //체크 위해서 1로 처리중 실제론 0로 처리 요망.

    public SolutionDetailResDto loadDetail(String solutionIdx) throws BaseException {

        UUID solIdx;

        try{
            solIdx = UUID.fromString(solutionIdx);
        }catch (Exception e){
            throw new BaseException(BaseErrorCode.UUID_ERROR);
        }

        Optional<SolutionDetail> solDetail = solutionDetailRepository.findBySolutionIdx_SolutionIdx(solIdx);

        solDetail.orElseThrow(()->new BaseException(BaseErrorCode.SOLUTION_DETAIL_NOT_FOUND));

        SolutionDetail solutionDetail = solDetail.get();

        List<SolutionImage> solutionImages = solutionImageRepository.findByDetailIdx_DetailIdx(solutionDetail.getDetailIdx());

        List<String> images = new ArrayList<>();

        if(solutionImages.isEmpty()){
            throw new BaseException(BaseErrorCode.SOLUTION_DETAIL_NOT_FOUND);
        }else{
            for(SolutionImage solutionImage : solutionImages){
                images.add(solutionImage.getImageUrl());
            }
        }

        return SolutionDetailResDto.builder()
                .detailIdx(solutionDetail.getDetailIdx())
                .detailHeader(solutionDetail.getDetailHeader())
                .imageUrls(images)
                .content(solutionDetail.getContent())
                .subContent(solutionDetail.getSubContent())
                .build();

    }






}
