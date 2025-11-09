package storage.file;

import storage.AbstractStorageTest;
import com.urise.storage.file.PathStorage;
import com.urise.storage.file.serializers.XmlSerializer;

public class XmlPathStorageTest extends AbstractStorageTest {

    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlSerializer()));
    }
}
