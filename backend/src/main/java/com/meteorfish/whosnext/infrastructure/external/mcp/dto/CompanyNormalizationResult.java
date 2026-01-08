package com.meteorfish.whosnext.infrastructure.external.mcp.dto;

public record CompanyNormalizationResult(
        String originalName,
        String standardizedName,
        double confidence,
        String reason
) {
}
