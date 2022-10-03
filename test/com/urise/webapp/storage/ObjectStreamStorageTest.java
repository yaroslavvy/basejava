package com.urise.webapp.storage;

class ObjectStreamStorageTest extends AbstractFileStorageTest {
    protected ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}