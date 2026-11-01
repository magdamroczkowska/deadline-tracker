package dev.tracker;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import dev.tracker.repository.CaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CaseRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private CaseRepository caseRepository;

    @BeforeEach
    void cleanUpDb() {
        caseRepository.deleteAll();
    }

    @Test
    void save_persistsCaseWithTimestamps() {
        CaseEntry saved = caseRepository.save(newCase("Test Client", CaseStatus.OPEN));

        assertThat(caseRepository.existsById(saved.getId())).isTrue();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void findByStatus_returnsOnlyCasesWithGivenStatus() {
        caseRepository.save(newCase("Client A", CaseStatus.OPEN));
        caseRepository.save(newCase("Client B", CaseStatus.OPEN));
        caseRepository.save(newCase("Client C", CaseStatus.RESPONDED));

        List<CaseEntry> openCases = caseRepository.findByStatus(CaseStatus.OPEN);

        assertThat(openCases).hasSize(2);
        assertThat(openCases).allMatch(c -> c.getStatus() == CaseStatus.OPEN);
    }

    private CaseEntry newCase(String clientName, CaseStatus status) {
        CaseEntry caseEntry = new CaseEntry();
        caseEntry.setClientName(clientName);
        caseEntry.setDescription("Test case description");
        caseEntry.setStatus(status);
        caseEntry.setDeadline(Instant.now().plus(7, ChronoUnit.DAYS));
        return caseEntry;
    }
}
