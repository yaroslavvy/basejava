package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public final Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    protected final Integer getSearchKey(String uuid) {
        return findIndex(uuid);
    }

    @Override
    protected final boolean isExist(Object searchKey) {
        return ((Integer) searchKey) >= 0;
    }

    @Override
    protected final void doSave(Object searchKey, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(searchKey, resume);
        ++size;
    }

    @Override
    protected final Resume doGet(Object searchKey) {
        return storage[((Integer) searchKey)];
    }

    @Override
    protected final void doDelete(Object searchKey) {
        fillDeletedElement((Integer) searchKey);
        storage[size - 1] = null;
        --size;
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage[((Integer) searchKey)] = resume;
    }

    protected abstract void insertResume(Object searchKey, Resume resume);

    protected abstract void fillDeletedElement(int index);

    protected abstract int findIndex(String uuid);
}
