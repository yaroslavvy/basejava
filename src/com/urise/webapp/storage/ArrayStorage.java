package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't store more than " + STORAGE_LIMIT + " resumes");
        } else if (findIndex(resume.getUuid()) >= 0) {
            System.out.println("Can't store resume uuid = " + resume.getUuid() + " because it already exists");
        } else {
            storage[size++] = resume;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            if (index != size - 1) {
                storage[index] = storage[size - 1];
            }
            storage[size - 1] = null;
            --size;
        } else {
            System.out.println("Resume uuid = " + uuid + " was not found");
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
