package dev.tracker.exceptions;

import dev.tracker.domain.CaseStatus;

import java.util.UUID;

public class InvalidCaseStatusException extends RuntimeException {
    public InvalidCaseStatusException(CaseStatus caseStatus) {
        super("Case is " + caseStatus + ", expected OPEN");
    }
}