package main.java.webapp.storage.file;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.file.PathStorage;
import webapp.storage.file.serializers.JsonSerializer;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonSerializer()));
    }
}
