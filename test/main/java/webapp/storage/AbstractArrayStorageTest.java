package main.java.webapp.storage;

import main.java.webapp.exeption.NotExistStorageException;
import main.java.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }
    @Test
    public void size() throws Exception{
       Assert.assertEquals(3, storage.size());
    }
    @Test
    public void clear() {
    }

    @Test
    public void update() {
    }

    @Test
    public void save() {
    }

    @BeforeClass
    public static void beforeClass() throws Exception {

    }

    @Test
    public void get() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getAll() {
    }

    @Test (expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }
}