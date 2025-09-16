package main.java.webapp.storage.file;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.file.PathStorage;
import webapp.storage.file.serializers.ObjectISerializer;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectISerializer()));
    }
}
