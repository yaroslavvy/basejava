package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public final void clear() {
        storage.clear();
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
    protected final boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    protected final void doSave(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    protected final Resume doGet(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected final void doDelete(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected final void doUpdate(String searchKey, Resume resume) {
        storage.replace(searchKey, resume);
    }

    @Override
    public final List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }
}
