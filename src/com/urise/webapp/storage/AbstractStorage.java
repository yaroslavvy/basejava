package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;

public abstract class AbstractStorage implements Storage {

    protected static final Comparator<Resume> RESUME_COMPARATOR_FULL_NAME_THEN_UUID = new Comparator<Resume>() {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getFullName().compareTo(o2.getFullName());
        }
    }.thenComparing((o1, o2) -> o1.compareTo(o2));

    @Override
    public final void save(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            doSave(searchKey, resume);
        }
    }

    @Override
    public final Resume get(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return doGet(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public final void delete(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            doDelete(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public final void update(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            doUpdate(searchKey, resume);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void doSave(Object searchKey, Resume resume);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume resume);
}
