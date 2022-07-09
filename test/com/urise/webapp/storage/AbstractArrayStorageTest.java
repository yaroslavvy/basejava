package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayStorageTest {
    private final AbstractArrayStorage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NOT_EXIST = "dummy";
    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final Resume RESUME_3 = new Resume(UUID_3);

    protected AbstractArrayStorageTest(AbstractArrayStorage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
        Resume[] expectedArray = new Resume[0];
        assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test
    void save() {
        Resume newResume = new Resume("uuid4");
        storage.save(newResume);
        assertGet(newResume);
        assertSize(4);
    }

    @Test
    void get() {
        assertAll(() -> assertGet(RESUME_1),
                () -> assertGet(RESUME_2),
                () -> assertGet(RESUME_3));
    }

    void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> {
            storage.get(UUID_1);
        });
    }

    @Test
    void getAll() {
        Resume[] expectedArray = new Resume[]{
                new Resume(UUID_1),
                new Resume(UUID_2),
                new Resume(UUID_3)
        };
        assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test
    void size() {
        assertSize(3);
    }

    void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }

    @Test
    void update() {
        Resume updatedResume = new Resume(UUID_1);
        storage.update(updatedResume);
        assertSize(3);
        assertSame(updatedResume, storage.get(UUID_1));
    }

    @Test
    void storageOverflow() {
        storage.clear();
        assertDoesNotThrow(() -> {
            for (int i = 0; i < storage.getStorageLimit(); i++) {
                storage.save(new Resume(UUID.randomUUID().toString()));
            }
        }, "Storage overflowed before reaching the maximum size");
        assertThrows(StorageException.class, () -> {
            storage.save(new Resume(UUID.randomUUID().toString()));
        });
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.get(UUID_NOT_EXIST);
        });
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.delete(UUID_NOT_EXIST);
        });
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.update(new Resume(UUID_NOT_EXIST));
        });
    }

    @Test
    void saveExist() {
        assertThrows(ExistStorageException.class, () -> {
            storage.save(RESUME_1);
        });
    }
}
