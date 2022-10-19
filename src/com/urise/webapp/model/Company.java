package com.urise.webapp.model;

import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
    private final List<Period> periods = new ArrayList<>();

    public Company() {
    }

    public Company(String name, String url) {
        this.name = Objects.requireNonNull(name, "company name must not be null");
        this.url = Objects.requireNonNull(url, "company url must not be null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) && Objects.equals(url, company.url) && Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, periods);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", periods=" + periods +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String description;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate beginDate;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;

        public Period() {
        }

        public Period(String title, String description, LocalDate beginDate, LocalDate endDate) {
            Objects.requireNonNull(title, "period title must not be null");
            Objects.requireNonNull(description, "period description must not be null");
            Objects.requireNonNull(beginDate, "period begin date must not be null");
            Objects.requireNonNull(endDate, "period end date must not be null");
            this.title = title;
            this.description = description;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getBeginDate() {
            return beginDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return Objects.equals(title, period.title) && Objects.equals(description, period.description) && Objects.equals(beginDate, period.beginDate) && Objects.equals(endDate, period.endDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description, beginDate, endDate);
        }

        @Override
        public String toString() {
            return "Period{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", beginDate=" + beginDate +
                    ", endDate=" + endDate +
                    '}';
        }
    }
}
