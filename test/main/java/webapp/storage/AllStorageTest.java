package main.java.webapp.storage;

import main.java.webapp.storage.file.FileStorageTest;
import main.java.webapp.storage.file.PathStorageTest;
import main.java.webapp.storage.memory.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                StorageTest.class,
                SortedStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class,
                MapResumeStorageTest.class,
                FileStorageTest.class,
                PathStorageTest.class
        })
public class AllStorageTest {
}
