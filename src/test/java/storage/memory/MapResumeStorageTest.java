package storage.memory;

import storage.AbstractStorageTest;
import com.urise.storage.file.PathStorage;
import com.urise.storage.file.serializers.ObjectSerializer;

public class MapResumeStorageTest extends AbstractStorageTest {
    public MapResumeStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectSerializer()));
    }
}
