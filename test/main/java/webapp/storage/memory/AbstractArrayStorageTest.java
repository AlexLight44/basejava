package main.java.webapp.storage.memory;

import main.java.webapp.exeption.StorageException;
import main.java.webapp.model.Resume;
import main.java.webapp.storage.AbstractStorageTest;
import main.java.webapp.storage.Storage;
import main.java.webapp.storage.file.ArrayStorage;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() {
        storage.clear();
        try {
            for (int i = 0; i < ArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume(UUID_NOT_EXISTING + i));
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume("overflow"));
    }
}
