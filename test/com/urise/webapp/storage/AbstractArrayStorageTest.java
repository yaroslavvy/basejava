package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void beforeEach(){
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    void save() {
        storage.save(new Resume("uuid4"));
        Resume[] expectedArray = new Resume[]{
                new Resume(UUID_1),
                new Resume(UUID_2),
                new Resume(UUID_3),
                new Resume("uuid4"),
        };
        Resume[] actualArray = Arrays.copyOf(storage.getAll(), storage.size());
        Arrays.sort(expectedArray);
        Arrays.sort(actualArray);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void get() {
        assertTrue(new Resume(UUID_1).equals(storage.get(UUID_1)));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        Resume[] expectedArray = new Resume[]{
                new Resume(UUID_2),
                new Resume(UUID_3)
        };
        Resume[] actualArray = Arrays.copyOf(storage.getAll(), storage.size());
        Arrays.sort(expectedArray);
        Arrays.sort(actualArray);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void getAll() {
        Resume[] expectedArray = new Resume[]{
                new Resume(UUID_1),
                new Resume(UUID_2),
                new Resume(UUID_3)
        };
        Resume[] actualArray = Arrays.copyOf(storage.getAll(), storage.size());
        Arrays.sort(expectedArray);
        Arrays.sort(actualArray);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void size() {
        assertEquals(3, storage.size());
    }

    @Test
    void update() {
        Resume newResume_1 = new Resume(UUID_1);
        storage.update(newResume_1);
        Resume[] expectedArray = new Resume[]{
                newResume_1,
                new Resume(UUID_2),
                new Resume(UUID_3)
        };
        Resume[] actualArray = Arrays.copyOf(storage.getAll(), storage.size());
        Arrays.sort(expectedArray);
        Arrays.sort(actualArray);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void getStorageOverflow() {
        assertDoesNotThrow(() -> {
            while (storage.size() < 10000) {
                storage.save(new Resume(UUID.randomUUID().toString()));
            }
        }, "Storage overflowed before reaching the maximum size");
        assertThrows(StorageException.class, () -> {
            storage.save(new Resume(UUID.randomUUID().toString()));
        });
    }

    @Test
    void getNotExistFromGetMethod() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.get("dummy");
        });
    }

    @Test
    void getNotExistFromDeleteMethod() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.delete("dummy");
        });
    }

    @Test
    void getNotExistFromUpdateMethod() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.update(new Resume("dummy"));
        });
    }

    @Test
    void getAlreadyExist() {
        assertThrows(ExistStorageException.class, () -> {
            storage.save(new Resume(UUID_1));
        });
    }
}
