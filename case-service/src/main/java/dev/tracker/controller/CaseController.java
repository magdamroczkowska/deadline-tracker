package dev.tracker.controller;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import dev.tracker.dto.CaseRequest;
import dev.tracker.dto.CaseResponse;
import dev.tracker.service.CaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {
    private final CaseService caseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaseResponse createCase(@Valid @RequestBody CaseRequest request) {
        return caseService.create(request);
    }

    @PostMapping("/{id}/respond")
    @ResponseStatus(HttpStatus.OK)
    public CaseResponse createResponse(@PathVariable UUID id) {
        caseService.respond(id);
        return CaseResponse.from(caseService.getById(id));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CaseResponse getById(@PathVariable UUID id) {
        return CaseResponse.from(caseService.getById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CaseResponse> getByStatus(@RequestParam CaseStatus status) {
        List<CaseEntry> caseEntries = caseService.findByStatus(status);

        return caseEntries.stream().map(CaseResponse::from).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        caseService.deleteById(id);
    }

}
