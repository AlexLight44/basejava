package main.java.webapp.storage.memory;

import main.java.webapp.storage.file.ArrayStorage;
import main.java.webapp.storage.memory.AbstractArrayStorageTest;

public class StorageTest extends AbstractArrayStorageTest {

    public StorageTest() {
        super(new ArrayStorage());
    }

}