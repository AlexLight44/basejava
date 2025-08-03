package main.java.webapp.storage;

import main.java.webapp.exeption.StorageException;
import main.java.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected static final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doSave(Resume r, Integer index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }else {
            endSave(r, index);
            size++;
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }


    public void doDelete(Integer index){
        endDelete(index);
        storage[size-1] = null;
        size--;
    }

    @Override
    protected void doUpdate(Resume r, Integer index){
        storage[index] = r;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected boolean isFind(Object index) {
        return (Integer) index >= 0;
    }
    protected abstract void endSave (Resume r, int index);
    protected abstract void endDelete(Integer index);
    protected abstract Integer getIndex(String uuid);
}
