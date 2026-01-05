package com.meteorfish.whosnext.api.company;

import com.meteorfish.whosnext.domain.company.Company;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        String industry,
        String location
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getLocation()
        );
    }
}
