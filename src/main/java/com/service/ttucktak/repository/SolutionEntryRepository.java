package com.service.ttucktak.repository;

import com.service.ttucktak.entity.SolutionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionEntryRepository extends JpaRepository<SolutionEntry, Long> {
}
