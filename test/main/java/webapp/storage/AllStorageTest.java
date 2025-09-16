package main.java.webapp.storage;

import main.java.webapp.storage.file.FileStorageTest;
import main.java.webapp.storage.file.PathStorageTest;
import main.java.webapp.storage.memory.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({StorageTest.class,
        SortedStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        FileStorageTest.class,
        PathStorageTest.class})
public class AllStorageTest {
}
