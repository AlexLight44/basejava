package main.java.webapp.storage;

import main.java.webapp.storage.strategy.ObjectStrategy;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStrategy()));
    }
}
