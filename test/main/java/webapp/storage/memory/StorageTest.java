package main.java.webapp.storage.memory;

import webapp.storage.file.ArrayStorage;

public class StorageTest extends AbstractArrayStorageTest {

    public StorageTest() {
        super(new ArrayStorage());
    }

}