package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int insertIndex, Resume resume) {
        for (int i = size; i > insertIndex; i--) {
            storage[i] = storage[i - 1];
        }
        storage[insertIndex] = resume;
    }

    @Override
    protected void fillDeletedElement(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

}
