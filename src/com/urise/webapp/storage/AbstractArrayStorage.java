package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public final void save(Resume resume) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't store more than " + STORAGE_LIMIT + " resumes");
            return;
        }
        int index = findIndex(resume.getUuid());
        if (index >= 0) {
            System.out.println("Can't store resume uuid = " + resume.getUuid() + " because it already exists");
        } else {
            insertResume(-(index + 1), resume);
            ++size;
        }
    }

    protected abstract void insertResume(int insertIndex, Resume resume);

    @Override
    public final Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("Resume uuid = " + uuid + " was not found");
        return null;
    }

    @Override
    public final void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            moveElements (index);
            storage[size - 1] = null;
            --size;
        } else {
            System.out.println("Resume uuid = " + uuid + " was not found");
        }
    }

    protected abstract void moveElements (int index);

    @Override
    public final Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Resume uuid = " + resume.getUuid() + " was not found");
        }
    }

    protected abstract int findIndex(String uuid);
}
