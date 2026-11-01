package dev.tracker.service;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import dev.tracker.dto.CaseRequest;
import dev.tracker.dto.CaseResponse;
import dev.tracker.exceptions.CaseNotFoundException;
import dev.tracker.exceptions.InvalidCaseStatusException;
import dev.tracker.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static dev.tracker.domain.CaseStatus.OPEN;
import static dev.tracker.domain.CaseStatus.RESPONDED;

@Service
@RequiredArgsConstructor
public class CaseService {
    private final CaseRepository repository;

    @Transactional
    public CaseResponse create(CaseRequest caseRequest) {
        CaseEntry caseEntry = new CaseEntry();

        caseEntry.setClientName(caseRequest.clientName());
        caseEntry.setDescription(caseRequest.description());
        caseEntry.setStatus(OPEN);
        caseEntry.setDeadline(caseRequest.deadline());

        return CaseResponse.from(repository.save(caseEntry));
    }

    @Transactional(readOnly = true)
    public CaseEntry getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CaseNotFoundException(id));
    }

    @Transactional
    public void respond(UUID id) {
        CaseEntry caseEntry = getById(id);
        if (caseEntry.getStatus() == OPEN) {
            caseEntry.setStatus(RESPONDED);
        } else {
            throw new InvalidCaseStatusException(caseEntry.getStatus());
        }
    }

    @Transactional(readOnly = true)
    public List<CaseEntry> findByStatus(CaseStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new CaseNotFoundException(id);
        }
        repository.deleteById(id);
    }

}
