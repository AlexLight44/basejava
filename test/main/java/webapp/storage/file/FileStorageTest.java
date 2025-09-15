package main.java.webapp.storage.file;

import main.java.webapp.storage.AbstractStorageTest;
import main.java.webapp.storage.file.serializers.ObjectISerializer;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectISerializer()));
    }
}
