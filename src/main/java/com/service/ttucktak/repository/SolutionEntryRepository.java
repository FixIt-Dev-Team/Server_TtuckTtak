package com.service.ttucktak.repository;

import com.service.ttucktak.entity.SolutionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionEntryRepository extends JpaRepository<SolutionEntry, Long> {

    public Optional<SolutionEntry> findByEntryIdx(Long entryIdx);

    public Optional<SolutionEntry> findBySurveyIdxAndResPattern(Long surveyIdx, Long resPattern);

}
