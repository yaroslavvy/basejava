package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public final void clear() {
        storage.clear();
    }

    @Override
    public final Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public final int size() {
        return storage.size();
    }

    @Override
    protected final String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected final boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

    @Override
    protected final void doSave(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected final Resume doGet(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected final void doDelete(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage.replace((String) searchKey, resume);
    }
}
