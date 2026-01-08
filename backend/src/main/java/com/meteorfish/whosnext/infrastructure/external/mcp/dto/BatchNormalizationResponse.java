package com.meteorfish.whosnext.infrastructure.external.mcp.dto;

import java.util.List;

public record BatchNormalizationResponse(
        List<CompanyNormalizationResult> results
) {
}
