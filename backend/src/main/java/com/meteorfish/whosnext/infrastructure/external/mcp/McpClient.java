package com.meteorfish.whosnext.infrastructure.external.mcp;

import com.meteorfish.whosnext.infrastructure.external.mcp.dto.BatchNormalizationResponse;
import com.meteorfish.whosnext.infrastructure.external.mcp.dto.CompanyNormalizationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class McpClient {

    private final RestClient restClient;

    public McpClient(@Value("${app.mcp.url}") String mcpUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(mcpUrl)
                .build();
    }

    public Map<String, String> normalizeCompanyNames(List<String> rawCompanyNames) {
        if (rawCompanyNames == null || rawCompanyNames.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            BatchNormalizationResponse response = restClient.post()
                    .uri("/api/mcp/company/normalize")
                    .body(rawCompanyNames)
                    .retrieve()
                    .body(BatchNormalizationResponse.class);

            if (response == null || response.results() == null) {
                return Collections.emptyMap();
            }

            return response.results().stream()
                    .collect(Collectors.toMap(
                            CompanyNormalizationResult::originalName,
                            CompanyNormalizationResult::standardizedName,
                            (existing, replacement) -> existing
                    ));

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
