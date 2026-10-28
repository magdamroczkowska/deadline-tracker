package dev.tracker.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CaseRequest(
        @NotBlank String clientName,
        @NotBlank String description,
        @NotNull @Future Instant deadline
){}