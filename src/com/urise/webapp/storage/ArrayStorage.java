package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    protected static final int STORAGE_LIMIT = 10000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't store more than " + STORAGE_LIMIT + " resumes");
        } else if (findIndex(resume.getUuid()) != -1) {
            System.out.println("Can't store resume uuid = " + resume.getUuid() + " because it already exists");
        } else {
            storage[size] = resume;
            ++size;
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("Resume uuid = " + uuid + " was not found");
        return null;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            for (; index < size - 1; index++) {
                storage[index] = storage[index + 1];
            }
            storage[size - 1] = null;
            --size;
            return;
        }
        System.out.println("Resume uuid = " + uuid + " was not found");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        }
        else {
            System.out.println("Resume uuid = " + resume.getUuid() + " was not found");
        }
    }

    protected int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
