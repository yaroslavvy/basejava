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
}
