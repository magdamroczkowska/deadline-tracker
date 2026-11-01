package dev.tracker;

import dev.tracker.domain.CaseStatus;
import dev.tracker.dto.CaseRequest;
import dev.tracker.dto.CaseResponse;
import dev.tracker.repository.CaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CaseControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private CaseRepository caseRepository;

    @BeforeEach
    void cleanUpDb() {
        caseRepository.deleteAll();
    }

    @Test
    void create_returns201WithOpenCase() {
        ResponseEntity<CaseResponse> response =
                rest.postForEntity("/api/cases", validRequest(), CaseResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(CaseStatus.OPEN);
    }

    @Test
    void create_withPastDeadline_returns400() {
        CaseRequest request = new CaseRequest("Test Client", "Test description",
                Instant.now().minus(1, ChronoUnit.DAYS));

        ResponseEntity<String> response = rest.postForEntity("/api/cases", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getById_returnsCase() {
        UUID id = createCase();

        ResponseEntity<CaseResponse> response =
                rest.getForEntity("/api/cases/{id}", CaseResponse.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id()).isEqualTo(id);
    }

    @Test
    void getById_unknownId_returns404() {
        ResponseEntity<String> response =
                rest.getForEntity("/api/cases/{id}", String.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getByStatus_returnsOpenCases() {
        createCase();
        createCase();

        ResponseEntity<CaseResponse[]> response =
                rest.getForEntity("/api/cases?status=OPEN", CaseResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void respond_changesStatusToResponded_andSecondRespondReturns409() {
        UUID id = createCase();

        ResponseEntity<CaseResponse> first =
                rest.postForEntity("/api/cases/{id}/respond", null, CaseResponse.class, id);
        ResponseEntity<String> second =
                rest.postForEntity("/api/cases/{id}/respond", null, String.class, id);

        assertThat(first.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(first.getBody().status()).isEqualTo(CaseStatus.RESPONDED);
        assertThat(second.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void delete_returns204_andCaseIsGone() {
        UUID id = createCase();

        ResponseEntity<Void> deleted =
                rest.exchange("/api/cases/{id}", HttpMethod.DELETE, null, Void.class, id);
        ResponseEntity<String> afterDelete =
                rest.getForEntity("/api/cases/{id}", String.class, id);

        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(afterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private CaseRequest validRequest() {
        return new CaseRequest("Test Client", "Test description",
                Instant.now().plus(7, ChronoUnit.DAYS));
    }

    private UUID createCase() {
        return rest.postForEntity("/api/cases", validRequest(), CaseResponse.class).getBody().id();
    }
}
