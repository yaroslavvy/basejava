package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (size < storage.length) {
            if (get(resume.getUuid()) == null) {
                storage[size] = resume;
                ++size;
            } else {
                notifyResumeWasNotSaved(resume.getUuid());
            }
        } else {
            notifyResumeStorageOverflow();
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        notifyResumeWasNotFound(uuid);
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                for (int j = i; j < size - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                storage[size - 1] = null;
                --size;
                return;
            }
        }
        notifyResumeWasNotFound(uuid);
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
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(resume.getUuid())) {
                storage[i] = resume;
                return;
            }
        }
        notifyResumeWasNotFound(resume.getUuid());
    }

    private void notifyResumeWasNotFound(String uuid) {
        System.out.println("Резюме с uuid = " + uuid + ", не было найдено");
    }

    private void notifyResumeStorageOverflow() {
        System.out.println("Невозможно сохранить более " + storage.length + " резюме");
    }

    private void notifyResumeWasNotSaved (String uuid) {
        System.out.println("Невозможно сохранить резюме с uuid = " + uuid + ", так как оно уже существует");
    }
}
