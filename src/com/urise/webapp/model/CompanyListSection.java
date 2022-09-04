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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyListSection that = (CompanyListSection) o;

        return companies != null ? companies.equals(that.companies) : that.companies == null;
    }

    @Override
    public int hashCode() {
        return companies != null ? companies.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CompanyListSection{" +
                "companies=" + companies +
                '}';
    }
}
