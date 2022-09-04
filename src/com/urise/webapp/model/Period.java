package com.urise.webapp.model;

import java.time.LocalDate;

public class Period {
    private String title;
    private String description;
    private LocalDate beginDate;
    private LocalDate endDate;

    public Period() {
    }

    public Period(String title, String description, LocalDate beginDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (title != null ? !title.equals(period.title) : period.title != null) return false;
        if (description != null ? !description.equals(period.description) : period.description != null) return false;
        if (beginDate != null ? !beginDate.equals(period.beginDate) : period.beginDate != null) return false;
        return endDate != null ? endDate.equals(period.endDate) : period.endDate == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
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
