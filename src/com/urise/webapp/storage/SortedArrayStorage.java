package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(Object searchKey, Resume resume) {
        int insertIndex = -(((Integer) searchKey) + 1);
        for (int i = size; i > insertIndex; i--) {
            storage[i] = storage[i - 1];
        }
        storage[insertIndex] = resume;
    }

    @Override
    protected void fillDeletedElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume(uuid, DEFAULT_FULL_NAME);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

}
