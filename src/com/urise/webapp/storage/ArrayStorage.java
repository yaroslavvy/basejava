package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int insertIndex, Resume resume) {
        storage[size] = resume;
    }

    @Override
    protected void fillDeletedElement(int index) {
        if (index != size - 1) {
            storage[index] = storage[size - 1];
        }
    }

    @Override
    protected int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
