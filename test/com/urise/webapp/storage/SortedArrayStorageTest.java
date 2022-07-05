package com.urise.webapp.storage;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class SortedArrayStorageTest extends AbstractArrayStorageTest{
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }
}