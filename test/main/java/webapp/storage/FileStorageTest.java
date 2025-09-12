package main.java.webapp.storage;

import main.java.webapp.storage.strategy.ObjectStrategy;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStrategy()));
    }
}
