package com.meteorfish.whosnext_mcp.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CompanyNormalizationService {

    private final ChatClient chatClient;

    public CompanyNormalizationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String normalize(String rawCompanyName) {
        String prompt = """
                You are a company name normalization assistant.
                Your task is to convert the given company name into its official, standard Korean name if possible.
                Remove unnecessary suffixes like "(주)", "Inc.", "Co., Ltd." etc.
                If the name is in English but has a well-known Korean name, convert it to Korean (e.g., "Samsung" -> "삼성전자").
                Only return the normalized name, nothing else.
                
                Input: %s
                Output:
                """.formatted(rawCompanyName);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .trim();
    }
}
