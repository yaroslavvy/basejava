package com.urise.webapp.storage;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStorageTest extends AbstractArrayStorageTest{
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }
}