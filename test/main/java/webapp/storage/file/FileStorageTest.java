package main.java.webapp.storage.file;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.file.FileStorage;
import webapp.storage.file.serializers.ObjectSerializer;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectSerializer()));
    }
}
