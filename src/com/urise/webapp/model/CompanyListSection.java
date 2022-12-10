package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanyListSection extends Section {
    private static final long serialVersionUID = 1L;
    private final List<Company> companies = new ArrayList<>();

    public CompanyListSection() {
    }

    public List<Company> getCompanies() {
        return new ArrayList<>(companies);
    }

    public void addCompany(Company company) {
        Objects.requireNonNull(company, "company must not be null");
        companies.add(company);
    }

    public static CompanyListSection cast (Section section) {
        return (CompanyListSection) section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyListSection that = (CompanyListSection) o;
        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companies);
    }

    @Override
    public String toString() {
        return "CompanyListSection{" +
                "companies=" + companies +
                '}';
    }
}
