package main.java.webapp.storage.memory;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.file.PathStorage;
import webapp.storage.file.serializers.ObjectSerializer;

public class MapResumeStorageTest extends AbstractStorageTest {
    public MapResumeStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectSerializer()));
    }
}
