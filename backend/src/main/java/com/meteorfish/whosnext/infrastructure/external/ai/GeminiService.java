package com.meteorfish.whosnext.infrastructure.external.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final ChatClient chatClient;

    // GeminiConfig에서 등록한 ChatClient 빈을 주입받음
    public GeminiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String normalizeCompanyName(String rawName) {
        String promptText = String.format("""
                당신은 기업 데이터 정규화 전문가입니다.
                사용자가 입력한 회사명: "%s"
                
                위 회사명의 공식적인 한국어 명칭(또는 가장 널리 쓰이는 표준명)을 단답형으로 알려주세요.
                설명이나 부가적인 말은 하지 말고 오직 회사명만 출력하세요.
                만약 회사명을 도저히 알 수 없다면 입력된 값을 그대로 반환하세요.
                
                예시:
                입력: 카뱅 -> 출력: 카카오뱅크
                입력: 삼전 -> 출력: 삼성전자
                입력: NAVER -> 출력: 네이버
                """, rawName);

        try {
            return chatClient.prompt()
                    .user(promptText)
                    .call()
                    .content()
                    .trim();
        } catch (Exception e) {
            // API 호출 실패 시 원본 반환 (Fallback)
            return rawName;
        }
    }
}
