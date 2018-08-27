package ru.shoma.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapStorageTest.class,
        PathStorageTest.class,
        FileStorageTest.class,
        XMLPathStorageTest.class,
        JsonPathStorageTest.class,
        DataPathStorageTest.class,
        SqlStorageTest.class
})

public class AllStorageTest {
}
