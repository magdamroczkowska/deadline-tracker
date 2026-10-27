package dev.tracker.service;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import dev.tracker.dto.CaseRequest;
import dev.tracker.exceptions.CaseNotFoundException;
import dev.tracker.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static dev.tracker.domain.CaseStatus.OPEN;
import static dev.tracker.domain.CaseStatus.RESPONDED;

@Service
@RequiredArgsConstructor
public class CaseService {
    private final CaseRepository repository;

    public void create(CaseRequest caseRequest) {
        CaseEntry caseEntry = new CaseEntry();

        caseEntry.setClientName(caseRequest.clientName());
        caseEntry.setDescription(caseRequest.description());
        caseEntry.setStatus(OPEN);
        caseEntry.setDeadline(caseRequest.deadline());

        repository.save(caseEntry);
    }

    public CaseEntry getById(UUID id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Case not found"));
    }

    public void respond(UUID id){
        CaseEntry caseEntry = getById(id);
        if(caseEntry.getStatus() == OPEN){
            caseEntry.setStatus(RESPONDED);
            repository.save(caseEntry);
        }
        else {
            throw new RuntimeException("Case is not open");
        }
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new CaseNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<CaseEntry> findByStatus(CaseStatus status) {
        return repository.findByStatus(status);
    }

}
