package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractContextStorageTest extends AbstractStorageTest {
    protected static final String STORAGE_DIR = "./storage";

    protected static Stream<FormatStrategy> strategyGenerator() {
        return Stream.of(new ObjectStreamFormatStrategy()
                //, new XMLFormatStrategy()
                //, new JSONFormatStrategy()
        );
    } //here point all strategy which test with all derivated classes

    protected AbstractContextStorageTest(Storage storage) {
        super(storage);
    }

    @Override
    void clear() {
    }

    @Override
    void save() {
    }

    @Override
    void get() {
    }

    @Override
    void delete() {
    }

    @Override
    void getAllSorted() {
    }

    @Override
    void size() {
    }

    @Override
    void update() {
    }

    @Override
    void getNotExist() {
    }

    @Override
    void deleteNotExist() {
    }

    @Override
    void updateNotExist() {
    }

    @Override
    void saveExist() {
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void clear(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.clear();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void save(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.save();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void get(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.get();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void delete(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.delete();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void getAllSorted(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.getAllSorted();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void size(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.size();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void update(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        Resume updatedResume = ResumeTestData.createAndFillResume(UUID_3, NEW_FULL_NAME_3);
        storage.update(updatedResume);
        assertSize(3);
        assertEquals(NEW_FULL_NAME_3, storage.get(UUID_3).getFullName());
        assertEquals(updatedResume, storage.get(UUID_3));
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void getNotExist(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.getNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void deleteNotExist(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.deleteNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void updateNotExist(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.updateNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void saveExist(FormatStrategy formatStrategy) {
        ((AbstractContextStorage) storage).setFormatStrategy(formatStrategy);
        super.saveExist();
    }
}