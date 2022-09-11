package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File dir;

    protected AbstractFileStorage(File dir) {
        Objects.requireNonNull(dir, "Direcory for resume must not be null!");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not directory!");
        }
        if (!dir.canRead() || !dir.canWrite()) {
            throw new IllegalArgumentException("Directory " + dir.getAbsolutePath() + " is not readable/writable!");
        }
        this.dir = dir;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(file, resume);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        Resume resume = null;
        try {
            resume = doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        return resume;
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        doDelete(file);
        doSave(file, resume);
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = dir.listFiles();
        List<Resume> resumeList = new ArrayList<>(size());
        for (File file : files) {
            if (file.isFile()) {
                try {
                    resumeList.add(doRead(file));
                } catch (IOException e) {
                    throw new StorageException("IO error", file.getName(), e);
                }
            }
        }
        return resumeList;
    }

    @Override
    public void clear() {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                doDelete(file);
            }
        }
    }

    @Override
    public int size() {
        int size = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                ++size;
            }
        }
        return size;
    }

    protected abstract void doWrite(File file, Resume resume) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}
