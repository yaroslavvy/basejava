package com.urise.webapp.storage;

import java.io.File;

class ContextFileStorageTest extends AbstractContextStorageTest {
    public ContextFileStorageTest() {
        super(new ContextFileStorage(new File(STORAGE_DIR), new ObjectStreamFormatStrategy())); //here can be any strategy
    }
}