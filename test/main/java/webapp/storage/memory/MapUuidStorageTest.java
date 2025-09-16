package main.java.webapp.storage.memory;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.memory.MapUuidStorage;


public class MapUuidStorageTest extends AbstractStorageTest {

    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }
}
