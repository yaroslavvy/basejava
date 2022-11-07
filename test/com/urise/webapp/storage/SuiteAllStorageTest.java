package com.urise.webapp.storage;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite All Storage Tests")
@SelectClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        XmlStreamFileStorageTest.class,
        XmlStreamPathStorageTest.class,
        JsonStreamFileStorageTest.class,
        JsonStreamPathStorageTest.class,
        DataStreamFileStorageTest.class,
        DataStreamPathStorageTest.class,
        SqlStorageTest.class
})
public class SuiteAllStorageTest {
}
