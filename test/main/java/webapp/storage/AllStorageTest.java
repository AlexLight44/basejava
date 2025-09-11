package main.java.webapp.storage;

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
                ObjectStreamStorageTest.class,
                ObjectStreamPathStorageTest.class
        })
public class AllStorageTest {
}
