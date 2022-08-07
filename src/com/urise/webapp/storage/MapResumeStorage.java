package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
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
    protected final Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected final boolean isExist(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected final void doSave(Resume searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected final Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected final void doDelete(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }

    @Override
    protected final void doUpdate(Resume searchKey, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    public final List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }
}
