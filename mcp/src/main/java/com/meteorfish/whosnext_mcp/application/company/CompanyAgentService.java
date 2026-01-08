package com.meteorfish.whosnext_mcp.application.company;

import com.meteorfish.whosnext_mcp.domain.BatchNormalizationResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyAgentService {

    private final ChatModel chatModel;

    public CompanyAgentService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public BatchNormalizationResponse getNormalizedNames(List<String> rawNames) {
        var converter = new BeanOutputConverter<>(BatchNormalizationResponse.class);

        String userMessage = """
                다음은 사용자가 입력한 기업 명칭 리스트입니다: %s
                
                지침:
                1. 각 명칭을 공식 기업 명칭으로 표준화하세요. (예: '삼성' -> '삼성전자', 'Apple' -> 'Apple Inc.')
                2. 명확하지 않은 경우 가장 유사한 대기업 명칭을 추론하세요.
                3. 반드시 JSON 형식으로 응답해야 하며, 입력된 순서대로 결과를 반환하세요.
                4. 결과에는 원본 이름(rawName)과 정규화된 이름(normalizedName)이 포함되어야 합니다.
                
                %s
                """;

        String finalPrompt = String.format(userMessage,
                    String.join(", ", rawNames),
                    converter.getFormat());

        ChatResponse response = chatModel.call(new Prompt(finalPrompt));

        return converter.convert(Objects.requireNonNull(response.getResult().getOutput().getText()));
    }
}
