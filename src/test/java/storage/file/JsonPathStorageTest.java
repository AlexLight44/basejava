package storage.file;

import storage.AbstractStorageTest;
import com.urise.storage.file.PathStorage;
import com.urise.storage.file.serializers.JsonSerializer;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonSerializer()));
    }
}
