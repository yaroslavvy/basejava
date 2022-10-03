package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractFileStorageTest extends AbstractStorageTest {
    protected AbstractFileStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    @Override
    void update() {
        Resume updatedResume = ResumeTestData.createAndFillResume(UUID_3, NEW_FULL_NAME_3);
        storage.update(updatedResume);
        assertSize(3);
        assertEquals(NEW_FULL_NAME_3, storage.get(UUID_3).getFullName());
        assertEquals(updatedResume, storage.get(UUID_3));
    }
}