package main.java.webapp.storage;

import main.java.webapp.storage.file.*;
import main.java.webapp.storage.memory.*;
import main.java.webapp.storage.sql.SqlStorageTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({StorageTest.class,
        SortedStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        FileStorageTest.class,
        PathStorageTest.class,
        XmlPathStorageTest.class,
        JsonPathStorageTest.class,
        DataPathStorageTest.class,
        SqlStorageTest.class})
public class AllStorageTest {
}
