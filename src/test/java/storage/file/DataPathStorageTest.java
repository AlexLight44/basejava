package storage.file;

import storage.AbstractStorageTest;
import com.urise.storage.file.PathStorage;
import com.urise.storage.file.serializers.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}
