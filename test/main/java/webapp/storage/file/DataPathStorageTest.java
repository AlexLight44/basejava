package main.java.webapp.storage.file;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.file.PathStorage;
import webapp.storage.file.serializers.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}
