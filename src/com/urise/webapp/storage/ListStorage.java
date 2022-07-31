package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public final void clear() {
        storage.clear();
    }

    @Override
    public final List<Resume> getAllSorted() {
        return storage.stream()
                .sorted(COMPARATOR_FULL_NAME_THEN_UUID)
                .collect(Collectors.toList());
    }

    @Override
    public final int size() {
        return storage.size();
    }

    @Override
    protected final Integer getSearchKey(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected final boolean isExist(Object searchKey) {
        return ((Integer) searchKey) >= 0;
    }

    @Override
    protected final void doSave(Object searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected final Resume doGet(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected final void doDelete(Object searchKey) {
        storage.remove(((Integer) searchKey).intValue());
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage.set((Integer) searchKey, resume);
    }
}
