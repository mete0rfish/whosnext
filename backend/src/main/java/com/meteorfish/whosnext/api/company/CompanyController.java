package com.meteorfish.whosnext.api.company;

import com.meteorfish.whosnext.application.company.CompanyAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Company API", description = "회사 정보 관리 API")
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyAppService companyAppService;

    public CompanyController(CompanyAppService companyAppService) {
        this.companyAppService = companyAppService;
    }

    @Operation(summary = "회사 등록", description = "새로운 회사를 등록합니다.")
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyAppService.create(request));
    }

    @Operation(summary = "회사 단건 조회", description = "ID로 특정 회사의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(CompanyResponse.from(companyAppService.getById(id)));
    }

    @Operation(summary = "회사 목록 조회", description = "등록된 모든 회사의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAll() {
        List<CompanyResponse> responses = companyAppService.getAll().stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "회사 정보 수정", description = "특정 회사의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody CompanyRequest request) {
        companyAppService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회사 삭제", description = "특정 회사를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyAppService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
