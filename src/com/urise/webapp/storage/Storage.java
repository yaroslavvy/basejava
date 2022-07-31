package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public interface Storage {

    Comparator<Resume> COMPARATOR_FULL_NAME_THEN_UUID = new Comparator<Resume>() {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getFullName().compareTo(o2.getFullName());
        }
    }.thenComparing((o1, o2) -> o1.compareTo(o2));

    void clear();

    void save(Resume resume);

    Resume get(String uuid);

    void delete(String uuid);

    List<Resume> getAllSorted();

    int size();

    void update(Resume resume);
}
