package storage.file;

import storage.AbstractStorageTest;
import com.urise.storage.file.FileStorage;
import com.urise.storage.file.serializers.ObjectSerializer;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectSerializer()));
    }
}
