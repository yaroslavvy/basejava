package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
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
    protected static final String UUID_1 = "11111111-1111-1111-1111-111111111111";
    protected static final String UUID_2 = "22222222-2222-2222-2222-222222222222";
    protected static final String UUID_3 = "33333333-3333-3333-3333-333333333333";
    protected static final String UUID_NOT_EXIST = "dummy";
    protected static final String FULL_NAME_1 = "Иван Петров";
    protected static final String FULL_NAME_2 = "Иван Петров";
    protected static final String FULL_NAME_3 = "Анна Сидорова";
    protected static final String NEW_FULL_NAME_3 = "Анна Петрова";
    protected static final Resume RESUME_1 = ResumeTestData.createAndFillResume(UUID_1, FULL_NAME_1);
    protected static final Resume RESUME_2 = ResumeTestData.createAndFillResume(UUID_2, FULL_NAME_2);
    protected static final Resume RESUME_3 = ResumeTestData.createAndFillResume(UUID_3, FULL_NAME_3);
    protected static final String STORAGE_DIR = Config.get().getStorageDir();

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
        Resume newResume = ResumeTestData.createAndFillResume(NEW_FULL_NAME_3);
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
                ResumeTestData.createAndFillResume(UUID_3, FULL_NAME_3),
                ResumeTestData.createAndFillResume(UUID_1, FULL_NAME_1),
                ResumeTestData.createAndFillResume(UUID_2, FULL_NAME_2)
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
        Resume updatedResume = ResumeTestData.createAndFillResume(UUID_3, NEW_FULL_NAME_3);
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
            storage.update(new Resume(NEW_FULL_NAME_3));
        });
    }

    @Test
    void saveExist() {
        assertThrows(ExistStorageException.class, () -> {
            storage.save(RESUME_1);
        });
    }
}