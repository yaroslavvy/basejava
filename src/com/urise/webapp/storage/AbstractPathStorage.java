package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        if (Files.isRegularFile(directory)) {
            throw new IllegalArgumentException(dir + " is not directory!");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException("Directory " + dir + " is not readable/writable!");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory + uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Path path, Resume resume) {
        try {
            Files.createFile(path);
            doWrite(new BufferedOutputStream(Files.newOutputStream(path)), resume);
        } catch (IOException e) {
            throw new StorageException("file save error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        Resume resume = null;
        try {
            resume = doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (Exception e) {
            throw new StorageException("file read error", path.getFileName().toString(), e);
        }
        return resume;
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("file delete error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            doWrite(new BufferedOutputStream(Files.newOutputStream(path)), resume);
        } catch (IOException e) {
            throw new StorageException("file update error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumeList = null;
        try {
            resumeList = Files.list(directory).filter(Files::isRegularFile)
                    .map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("IO error", null, e);
        }
        return resumeList;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).filter(Files::isRegularFile).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Unsuccessful storage clear. IO error", null);
        }
    }

    @Override
    public int size() {
        int size = 0;
        try {
            size = (int) (Files.list(directory).filter(Files::isRegularFile).filter(path -> {
                try {
                    doGet(path);
                    return true;
                } catch (StorageException e) {
                    return false;
                }
            }).count());
        } catch (IOException e) {
            throw new StorageException("IO error", null, e);
        }
        return size;
    }

    protected abstract void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    protected abstract Resume doRead(InputStream inputStream) throws Exception;
}
