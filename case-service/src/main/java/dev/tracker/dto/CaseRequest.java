package dev.tracker.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record CaseRequest(
    @NotBlank String clientName,
    @NotBlank String description,
    @Future Instant deadline
) {
    public CaseRequest {
        if (deadline.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }
    }
}