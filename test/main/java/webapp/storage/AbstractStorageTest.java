package main.java.webapp.storage;

import main.java.webapp.exeption.NotExistStorageException;
import main.java.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractStorageTest {

    protected static final int INITIAL_CAPACITY = 3;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String UUID_NOT_EXISTING = "dummy";

    protected static final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, "Name1");
    protected static final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, "Name2");
    protected static final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, "Name3");
    protected static final Resume RESUME_4 = ResumeTestData.createResume(UUID_4, "Name4");

    protected static final File STORAGE_DIR = new File("C:\\projects\\basejava\\storage");
    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(INITIAL_CAPACITY);
    }

    public void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assert.assertEquals(Collections.emptyList(), storage.getAllSorted());
    }

    @Test
    public void update() {
        Resume update = ResumeTestData.createResume(UUID_3, "Name3");
        storage.update(update);
        Assert.assertEquals(update, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_NOT_EXISTING));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(INITIAL_CAPACITY + 1);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    public void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXISTING);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertSize(INITIAL_CAPACITY - 1);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXISTING);
    }

    @Test
    public void getAll() {
        List<Resume> expected = new ArrayList<>(List.of(RESUME_1, RESUME_2, RESUME_3));
        Assert.assertEquals(expected, storage.getAllSorted());
    }


}