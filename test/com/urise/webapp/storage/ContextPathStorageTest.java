package com.urise.webapp.storage;

class ContextPathStorageTest extends AbstractContextStorageTest {
    public ContextPathStorageTest() {
        super(new ContextPathStorage(STORAGE_DIR, new ObjectStreamFormatStrategy())); //here can be any strategy
    }
}