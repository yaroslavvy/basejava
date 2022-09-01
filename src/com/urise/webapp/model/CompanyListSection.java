package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class CompanyListSection extends Section {
    private List<Company> companies = new ArrayList<>();

    public List<Company> getCompanies() {
        return new ArrayList<>(companies);
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
