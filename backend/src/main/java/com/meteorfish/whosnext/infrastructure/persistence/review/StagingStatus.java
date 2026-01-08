package com.meteorfish.whosnext.infrastructure.persistence.review;

public enum StagingStatus {
    PENDING,    // 수집됨 (AI 처리 전)
    ANALYZED,   // AI 분석 완료
    APPROVED,   // 최종 승인
    REJECTED    // 거절됨
}
