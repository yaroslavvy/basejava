package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.JsonStreamStrategy;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonStreamFileStorageTest extends AbstractStorageTest {
    public JsonStreamFileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new JsonStreamStrategy()));
    }

    @Override
    void update() {
        Resume updatedResume = ResumeTestData.createAndFillResume(UUID_3, NEW_FULL_NAME_3);
        storage.update(updatedResume);
        assertSize(3);
        assertEquals(NEW_FULL_NAME_3, storage.get(UUID_3).getFullName());
        assertEquals(updatedResume, storage.get(UUID_3));
    }
}