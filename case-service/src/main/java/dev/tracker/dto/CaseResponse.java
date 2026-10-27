package dev.tracker.dto;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;

import java.time.Instant;
import java.util.UUID;

public record CaseResponse(UUID id, String clientName, String description, Instant deadline, Instant created_at, Instant updated_at, CaseStatus status) {

    public static CaseResponse from(CaseEntry e) {
        return new CaseResponse(e.getId(), e.getClientName(), e.getDescription(), e.getDeadline(), e.getCreated_at(), e.getUpdated_at(), e.getStatus());
    }
}
