package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private String website;
    private List<Period> periods = new ArrayList<>();

    public Company() {
    }

    public Company(String name, String website, List<Period> periods) {
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Period> getPeriods() {
        return new ArrayList<>(periods);
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
}
