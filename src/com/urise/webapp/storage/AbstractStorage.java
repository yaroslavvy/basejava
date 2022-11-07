package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOGGER;

    static {
        Configurator.initialize(null, Config.get().getLogConfig());
        LOGGER = LoggerFactory.getLogger(AbstractStorage.class);
    }

    protected static final Comparator<Resume> RESUME_COMPARATOR_FULL_NAME_THEN_UUID =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected static final String DEFAULT_FULL_NAME = "default full name";

    @Override
    public final void save(Resume resume) {
        LOGGER.info("resume uuid: " + resume);
        SK searchKey = getNotExistingSearchKey(resume.getUuid());
        try {
            doSave(searchKey, resume);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);// TODO Del me. I know it bad practice. ↓↓↓
            // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
            throw e;
        }

    }

    @Override
    public final Resume get(String uuid) {
        LOGGER.info("resume uuid: " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public final void delete(String uuid) {
        LOGGER.info("resume uuid: " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    public final List<Resume> getAllSorted() {
        List<Resume> list = doCopyAll();
        list.sort(RESUME_COMPARATOR_FULL_NAME_THEN_UUID);
        return list;
    }

    @Override
    public final void update(Resume resume) {
        LOGGER.info("resume uuid: " + resume);
        SK searchKey = getExistingSearchKey(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    private SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            NotExistStorageException exception = new NotExistStorageException(uuid);
            LOGGER.error(exception.getMessage(), exception);// TODO Del me. I know it bad practice. ↓↓↓
            // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
            throw exception;
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            ExistStorageException exception = new ExistStorageException(uuid);
            LOGGER.error(exception.getMessage(), exception);// TODO Del me. I know it bad practice. ↓↓↓
            // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
            throw exception;
        }
        return searchKey;
    }

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void doSave(SK searchKey, Resume resume);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume resume);

    protected abstract List<Resume> doCopyAll();
}
