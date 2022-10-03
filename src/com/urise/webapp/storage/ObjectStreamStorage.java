package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {
    protected ObjectStreamStorage(File directory) {
        super(directory);
    }

    @Override
    protected void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(InputStream inputStream) throws Exception {
        Resume result = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            result = (Resume) (objectInputStream.readObject());
        }
        return result;
    }
}
