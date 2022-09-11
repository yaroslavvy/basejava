package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.StorageException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void storageOverflow() {
        storage.clear();
        assertDoesNotThrow(() -> {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(ResumeTestData.createAndFillResume(AbstractArrayStorage.DEFAULT_FULL_NAME));
            }
        }, "Storage overflowed before reaching the maximum size");
        assertThrows(StorageException.class, () -> {
            storage.save(ResumeTestData.createAndFillResume(AbstractArrayStorage.DEFAULT_FULL_NAME));
        });
    }
}
