package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't store more than " + STORAGE_LIMIT + " resumes");
        } else {
            int index = findIndex(resume.getUuid());
            if (index >= 0) {
                System.out.println("Can't store resume uuid = " + resume.getUuid() + " because it already exists");
            } else {
                int insertIndex = -(index + 1);
                for (int i = size; i > insertIndex; i--) {
                    storage[i] = storage[i - 1];
                }
                storage[insertIndex] = resume;
                ++size;
            }
        }
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            for (int i = index; i < size - 1; i++) {
                storage[i] = storage[i + 1];
            }
            storage[size - 1] = null;
            --size;
        } else {
            System.out.println("Resume uuid = " + uuid + " was not found");
        }
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

}
