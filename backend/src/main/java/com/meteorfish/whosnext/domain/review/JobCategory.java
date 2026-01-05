package com.meteorfish.whosnext.domain.review;

public enum JobCategory {
    MARKETING("마케팅"),
    BUSINESS("경영"),
    DESIGN("디자인"),
    SALES("영업"),
    MEDIA("미디어"),
    ENGINEERING("엔지니어"),
    DEVELOPMENT("개발"),
    OTHER("기타");

    private final String description;

    JobCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
