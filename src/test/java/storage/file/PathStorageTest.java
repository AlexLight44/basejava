package storage.file;

import storage.AbstractStorageTest;
import com.urise.storage.file.PathStorage;
import com.urise.storage.file.serializers.ObjectSerializer;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectSerializer()));
    }
}
