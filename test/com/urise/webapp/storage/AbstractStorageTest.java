package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractStorageTest {
    protected final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NOT_EXIST = "dummy";
    private static final String FULL_NAME_1 = "Иван Петров";
    private static final String FULL_NAME_2 = "Иван Петров";
    private static final String FULL_NAME_3 = "Анна Сидорова";
    private static final String NEW_FULL_NAME_3 = "Анна Петрова";
    private static final Resume RESUME_1 = new Resume(UUID_1, FULL_NAME_1);
    private static final Resume RESUME_2 = new Resume(UUID_2, FULL_NAME_2);
    private static final Resume RESUME_3 = new Resume(UUID_3, FULL_NAME_3);

    protected AbstractStorageTest(Storage storage) {
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
        List<Resume> expectedList = new ArrayList<>();
        assertIterableEquals(expectedList, storage.getAllSorted());
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
    void getAllSorted() {
        List<Resume> expectedList = Arrays.asList(
                new Resume(UUID_3, FULL_NAME_3),
                new Resume(UUID_1, FULL_NAME_1),
                new Resume(UUID_2, FULL_NAME_2)
        );
        List<Resume> actualList = storage.getAllSorted();
        assertIterableEquals(expectedList, actualList);
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
        Resume updatedResume = new Resume(UUID_3, NEW_FULL_NAME_3);
        storage.update(updatedResume);
        assertSize(3);
        assertSame(NEW_FULL_NAME_3, storage.get(UUID_3).getFullName());
        assertSame(updatedResume, storage.get(UUID_3));
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