package com.meteorfish.whosnext.domain.company;

import java.util.UUID;

public class Company {
    private final UUID id;
    private String name;
    private String industry;
    private String location;

    public Company(UUID id, String name, String industry, String location) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.location = location;
    }

    public void update(String name, String industry, String location) {
        validateName(name);
        this.name = name;
        this.industry = industry;
        this.location = location;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회사 이름은 필수입니다.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getLocation() {
        return location;
    }
}
