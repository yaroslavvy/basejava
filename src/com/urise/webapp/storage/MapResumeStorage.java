package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapResumeStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public final void clear() {
        storage.clear();
    }

    @Override
    public final List<Resume> getAllSorted() {
        return storage.values().stream()
                .sorted(RESUME_COMPARATOR_FULL_NAME_THEN_UUID)
                .collect(Collectors.toList());
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
    protected final boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected final void doSave(Object searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected final Resume doGet(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected final void doDelete(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }
}
