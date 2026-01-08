package com.meteorfish.whosnext_mcp.domain;

public record CompanyNormalizationResult(
        String originalName,
        String standardizedName,
        double confidence,
        String reason
) {
}
