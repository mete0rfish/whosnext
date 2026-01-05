package com.meteorfish.whosnext.application.company;

import com.meteorfish.whosnext.api.company.CompanyRequest;
import com.meteorfish.whosnext.domain.company.Company;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyEntity;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyAppService {
    private final CompanyRepository companyRepository;

    public CompanyAppService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public UUID create(CompanyRequest request) {
        Company company = new Company(null, request.name(), request.industry(), request.location());
        CompanyEntity entity = CompanyEntity.from(company);
        return companyRepository.save(entity).getId();
    }

    @Transactional(readOnly = true)
    public Company getById(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회사를 찾을 수 없습니다."))
                .toDomain();
    }

    @Transactional(readOnly = true)
    public List<Company> getAll() {
        return companyRepository.findAll().stream()
                .map(CompanyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(UUID id, CompanyRequest request) {
        CompanyEntity entity = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회사를 찾을 수 없습니다."));

        Company company = entity.toDomain();
        company.update(request.name(), request.industry(), request.location());

        entity.updateFromDomain(company);
    }

    @Transactional
    public void delete(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 회사가 존재하지 않습니다.");
        }
        companyRepository.deleteById(id);
    }
}
