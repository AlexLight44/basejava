package main.java.webapp.storage;

import main.java.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {


    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void doSave(Resume r, int index) {
        int indexDo = -index - 1;
        System.arraycopy(storage, indexDo, storage, indexDo + 1, size - indexDo);
        storage[indexDo] = r;
    }

    @Override
    protected void doDelete(int index) {
        int shift = size - index - 1;
        if (shift > 0) {
            System.arraycopy(storage, index + 1, storage, index, shift);
        }
    }
}
