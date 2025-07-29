package main.java.webapp.storage;

import main.java.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {


    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void doSave(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected void doDelete(int index) {
        storage[index] = storage[size - 1];
    }
}
