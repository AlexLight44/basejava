package storage.memory;

import storage.AbstractStorageTest;
import com.urise.storage.memory.MapUuidStorage;


public class MapUuidStorageTest extends AbstractStorageTest {

    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }
}
