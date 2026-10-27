package dev.tracker.controller;

import dev.tracker.domain.CaseEntry;
import dev.tracker.domain.CaseStatus;
import dev.tracker.dto.CaseRequest;
import dev.tracker.dto.CaseResponse;
import dev.tracker.service.CaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases")
@AllArgsConstructor
public class CaseController {
    CaseService caseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCase(@Valid @RequestBody CaseRequest request) {
         caseService.create(request);
    }

    @PostMapping("/{id}/respond")
    @ResponseStatus(HttpStatus.CREATED)
    public CaseResponse createResponse(@RequestParam UUID id) {
         caseService.respond(id);
         return CaseResponse.from(caseService.getById(id));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CaseResponse getById(@PathVariable UUID id) {
        CaseEntry caseEntry = caseService.getById(id);

        return CaseResponse.from(caseEntry);
    }

    @GetMapping("?status=OPEN")
    @ResponseStatus(HttpStatus.OK)
    public List<CaseResponse> getByOpenStatus(@RequestParam CaseStatus status) {
        List<CaseEntry> caseEntries = caseService.findByStatus(status);

        return caseEntries.stream().map(CaseResponse::from).toList();
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam UUID id) {
        caseService.deleteById(id);
    }

}
