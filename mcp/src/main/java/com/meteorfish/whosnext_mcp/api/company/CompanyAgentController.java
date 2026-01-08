package com.meteorfish.whosnext_mcp.api.company;

import com.meteorfish.whosnext_mcp.application.company.CompanyAgentService;
import com.meteorfish.whosnext_mcp.domain.BatchNormalizationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mcp")
public class CompanyAgentController {

    private final CompanyAgentService agentService;

    public CompanyAgentController(CompanyAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/company/normalize")
    public BatchNormalizationResponse normalize(@RequestBody List<String> names) {
        return agentService.getNormalizedNames(names);
    }
}
