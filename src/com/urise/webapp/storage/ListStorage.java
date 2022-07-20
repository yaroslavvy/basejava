package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public final void clear() {
        storage.clear();
    }

    @Override
    public final Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public final int size() {
        return storage.size();
    }

    @Override
    protected final Object getSearchKey(String uuid) {
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
        return storage.get(((Integer) searchKey));
    }

    @Override
    protected final void doDelete(Object searchKey) {
        storage.remove(((Integer) searchKey).intValue());
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage.set(((Integer) searchKey), resume);
    }
}
