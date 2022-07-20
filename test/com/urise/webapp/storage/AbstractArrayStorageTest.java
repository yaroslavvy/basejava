package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
                storage.save(new Resume(UUID.randomUUID().toString()));
            }
        }, "Storage overflowed before reaching the maximum size");
        assertThrows(StorageException.class, () -> {
            storage.save(new Resume(UUID.randomUUID().toString()));
        });
    }
}
