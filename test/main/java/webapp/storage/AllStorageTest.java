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
                    MapResumeStorageTest.class
            })
    public class AllStorageTest {
    }
