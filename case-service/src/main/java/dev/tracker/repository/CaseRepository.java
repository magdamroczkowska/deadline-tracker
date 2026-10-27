package dev.tracker.repository;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CaseRepository extends JpaRepository<CaseEntry, UUID> {

    List<CaseEntry> findByStatus(CaseStatus status);
}

