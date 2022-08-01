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
    public final int size() {
        return storage.size();
    }

    @Override
    protected final Integer getSearchKey(String uuid) {
        return storage.indexOf(new Resume(uuid, DEFAULT_FULL_NAME));
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

    @Override
    public final List<Resume> doCopyAll() {
        return new ArrayList<>(storage);
    }
}
