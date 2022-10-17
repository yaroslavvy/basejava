package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
        for (int i = 0; i < storage.size(); ++i) {
            if (storage.get(i) != null && storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected final boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    protected final void doSave(Integer searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected final Resume doGet(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected final void doDelete(Integer searchKey) {
        storage.remove(searchKey.intValue());

    }

    @Override
    protected final void doUpdate(Integer searchKey, Resume resume) {
        storage.set(searchKey, resume);
    }

    @Override
    public final List<Resume> doCopyAll() {
        return new ArrayList<>(storage);
    }
}
