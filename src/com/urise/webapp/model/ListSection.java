package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends Section {
    private List<String> list = new ArrayList<>();

    public List<String> getList() {
        return new ArrayList<>(list);
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
