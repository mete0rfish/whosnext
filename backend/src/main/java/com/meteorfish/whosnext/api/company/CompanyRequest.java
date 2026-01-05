package com.meteorfish.whosnext.api.company;

public record CompanyRequest(
        String name,
        String industry,
        String location
) {
}
