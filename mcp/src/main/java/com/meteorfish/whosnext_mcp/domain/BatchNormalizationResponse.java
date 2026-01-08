package com.meteorfish.whosnext_mcp.domain;

import java.util.List;

public record BatchNormalizationResponse(
        List<CompanyNormalizationResult> results
) {
}
