package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JSONFormatStrategy implements FormatStrategy {
    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        //TODO impl
    }

    @Override
    public Resume doRead(InputStream inputStream) throws Exception {
        //TODO impl
        return null;
    }
}
