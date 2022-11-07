package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlStorageTest extends AbstractStorageTest {
    protected SqlStorageTest() {
        super(new SqlStorage(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword()));
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

    @Test
    @Override
    void saveExist() {
        assertThrows(StorageException.class, () -> {
            storage.save(RESUME_1);
        });
    }
}