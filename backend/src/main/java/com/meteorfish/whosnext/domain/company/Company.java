package com.meteorfish.whosnext.domain.company;

import java.util.UUID;

public class Company {
    private final UUID id;
    private final String name;
    private final String industry;
    private final String location;

    public Company(UUID id, String name, String industry, String location) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.location = location;
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
