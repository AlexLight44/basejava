package storage.memory;


import storage.AbstractStorageTest;
import org.junit.jupiter.api.Test;
import com.urise.exeption.StorageException;
import com.urise.model.Resume;
import com.urise.storage.Storage;
import com.urise.storage.memory.ArrayStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void saveStorageException() {
        storage.clear();
        for (int i = 0; i < ArrayStorage.STORAGE_LIMIT; i++) {
            storage.save(new Resume("uuid" + i));
        }
        assertThrows(StorageException.class, () -> {
            storage.save(new Resume("overflow"));
        });
    }
}
