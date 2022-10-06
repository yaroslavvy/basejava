package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private StreamStrategy streamStrategy;

    public PathStorage(String dir, StreamStrategy streamStrategy) {
        directory = Paths.get(dir);
        setFormatStrategy(streamStrategy);
        if (Files.isRegularFile(directory)) {
            throw new IllegalArgumentException(dir + " is not directory!");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException("directory " + dir + " is not readable/writable!");
        }
    }

    public void setFormatStrategy(StreamStrategy streamStrategy) {
        Objects.requireNonNull(streamStrategy, "format strategy for storage must not be null!");
        this.streamStrategy = streamStrategy;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Path path, Resume resume) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("file save error", path.getFileName().toString(), e);
        }
        doUpdate(path, resume);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamStrategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (Exception e) {
            throw new StorageException("file read error", path.getFileName().toString(), e);
        }
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
            streamStrategy.doWrite(new BufferedOutputStream(Files.newOutputStream(path)), resume);
        } catch (IOException e) {
            throw new StorageException("file update error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getFileStream().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getFileStream().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getFileStream().count();
    }

    private Stream<Path> getFileStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("unsuccessful getting file list from directory: " + directory, null, e);
        }
    }
}
