package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory for resume must not be null!");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory!");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException("Directory " + directory.getAbsolutePath() + " is not readable/writable!");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(new BufferedOutputStream(new FileOutputStream(file)), resume);
        } catch (IOException e) {
            throw new StorageException("file save error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        Resume resume = null;
        try {
            resume = doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (Exception e) {
            throw new StorageException("file read error", file.getName(), e);
        }
        return resume;
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("file delete error", file.getName());
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            doWrite(new BufferedOutputStream(new FileOutputStream(file)), resume);
        } catch (IOException e) {
            throw new StorageException("file update error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("IO error", null);
        }
        List<Resume> resumeList = new ArrayList<>(size());
        for (File file : files) {
            if (file.isFile()) {
                resumeList.add(doGet(file));
            }
        }
        return resumeList;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Unsuccessful storage clear. IO error", null);
        }
        for (File file : files) {
            if (file.isFile()) {
                doDelete(file);
            }
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("IO error", null);
        }
        int size = 0;
        for (File file : files) {
            try {
                doGet(file);
                ++size;
            } catch (StorageException e) {
            }
        }
        return size;
    }

    protected abstract void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    protected abstract Resume doRead(InputStream inputStream) throws Exception;
}
