package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.ObjectStreamStrategy;
import com.urise.webapp.storage.serializer.StreamStrategy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileStorageTest extends AbstractStorageTest {
    public FileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new ObjectStreamStrategy())); //here can be any strategy
    }

    private static Stream<StreamStrategy> strategyGenerator() {
        return Stream.of(new ObjectStreamStrategy());//here point all strategy which should be tested
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
    void clear(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.clear();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void save(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.save();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void get(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.get();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void delete(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.delete();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void getAllSorted(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.getAllSorted();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void size(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.size();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void update(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        Resume updatedResume = ResumeTestData.createAndFillResume(UUID_3, NEW_FULL_NAME_3);
        storage.update(updatedResume);
        assertSize(3);
        assertEquals(NEW_FULL_NAME_3, storage.get(UUID_3).getFullName());
        assertEquals(updatedResume, storage.get(UUID_3));
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void getNotExist(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.getNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void deleteNotExist(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.deleteNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void updateNotExist(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.updateNotExist();
    }

    @ParameterizedTest
    @MethodSource("strategyGenerator")
    void saveExist(StreamStrategy streamStrategy) {
        ((FileStorage) storage).setFormatStrategy(streamStrategy);
        super.saveExist();
    }
}