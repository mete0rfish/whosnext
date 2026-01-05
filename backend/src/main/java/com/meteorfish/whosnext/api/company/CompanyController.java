package com.meteorfish.whosnext.api.company;

import com.meteorfish.whosnext.application.company.CompanyAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyAppService companyAppService;

    public CompanyController(CompanyAppService companyAppService) {
        this.companyAppService = companyAppService;
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyAppService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(CompanyResponse.from(companyAppService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAll() {
        List<CompanyResponse> responses = companyAppService.getAll().stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody CompanyRequest request) {
        companyAppService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyAppService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
