package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private StreamStrategy streamStrategy;

    public FileStorage(File directory, StreamStrategy streamStrategy) {
        Objects.requireNonNull(directory, "directory for resume must not be null!");
        setFormatStrategy(streamStrategy);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory!");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException("directory " + directory.getAbsolutePath() + " is not readable/writable!");
        }
        this.directory = directory;
    }

    public void setFormatStrategy(StreamStrategy streamStrategy) {
        Objects.requireNonNull(streamStrategy, "format strategy for storage must not be null!");
        this.streamStrategy = streamStrategy;
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
        } catch (IOException e) {
            throw new StorageException("file save error", file.getName(), e);
        }
        doUpdate(file, resume);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return streamStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (Exception e) {
            throw new StorageException("file read error", file.getName(), e);
        }
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
            streamStrategy.doWrite(new BufferedOutputStream(new FileOutputStream(file)), resume);
        } catch (IOException e) {
            throw new StorageException("file update error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumeList = new ArrayList<>(size());
        for (File file : getFileArray()) {
            resumeList.add(doGet(file));
        }
        return resumeList;
    }

    @Override
    public void clear() {
        for (File file : getFileArray()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getFileArray().length;
    }

    private File[] getFileArray() {
        try {
            return Objects.requireNonNull(directory.listFiles());
        } catch (Exception e) {
            throw new StorageException("unsuccessful getting file list from directory: " + directory.getAbsolutePath(), e);
        }
    }
}
