package main.java.webapp.storage.memory;

import main.java.webapp.storage.memory.AbstractArrayStorageTest;
import main.java.webapp.storage.memory.SortedArrayStorage;

public class SortedStorageTest extends AbstractArrayStorageTest {

    public SortedStorageTest() {
        super(new SortedArrayStorage());
    }

}