package dev.tracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "case_entry")
@Getter
@Setter
@NoArgsConstructor
public class CaseEntry {

    @Id @GeneratedValue
    private UUID id;
    private String clientName;
    private String description;
    @Enumerated(EnumType.STRING)
    private CaseStatus status;
    private Instant deadline;
    private Instant created_at;
    private Instant updated_at;

    @PrePersist
    void prePersist() {
        created_at = Instant.now();
        updated_at = Instant.now();
    }

    @PostPersist
    void postPersist() {
        updated_at = Instant.now();
    }
}
