package main.java.webapp.storage;

import main.java.webapp.exeption.ExistStorageException;
import main.java.webapp.exeption.NotExistStorageException;
import main.java.webapp.exeption.StorageException;
import main.java.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;


    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume update = new Resume(UUID_3);
        storage.update(update);
        Assert.assertEquals(update, storage.get("uuid3"));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("uuid_6"));
    }

    @Test
    public void save() {
        Resume saveResume = new Resume("uuid4");
        storage.save(saveResume);
        Assert.assertEquals(saveResume, storage.get("uuid4"));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(storage.get("uuid3"));
    }

    @Test
    public void saveStorageExeption() {
        try {
            for (int i = 4; i < 10000; i++) {
                storage.save(new Resume("uuid" + i));
            }
        } catch (StorageException e) {
            Assert.fail("overflow prematurely" + e.getMessage());
        }
        try {
            storage.save(new Resume("uuid10001"));
        } catch (StorageException e) {
        }
    }

    @Test
    public void get() {
        Resume getResume = new Resume("uuid3");
        Assert.assertEquals(getResume, storage.get("uuid3"));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("uud6");
    }

    @Test
    public void getAll() {
        Resume[] all = storage.getAll();
        Assert.assertEquals(3, all.length);
        Assert.assertEquals(UUID_1, all[0].getUuid());
        Assert.assertEquals(UUID_2, all[1].getUuid());
        Assert.assertEquals(UUID_3, all[2].getUuid());
    }


}