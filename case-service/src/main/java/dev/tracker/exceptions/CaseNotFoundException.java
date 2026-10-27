package dev.tracker.exceptions;

import java.util.UUID;

public class CaseNotFoundException extends RuntimeException {
    public CaseNotFoundException(UUID id) {
        super("Case " + id + " not found");
    }
}