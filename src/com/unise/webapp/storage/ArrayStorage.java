package com.unise.webapp.storage;
import com.unise.webapp.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    static final int STORAGE_LIMIT = 10000;
    int size;
    protected final Resume[] storage = new Resume[10000];

    public int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int index = findIndex(resume.uuid);
        if (index == -1) {
            System.out.println("Не найден ваш " + resume.uuid);
        } else {
            storage[index] = resume;
        }
    }

    public void save(Resume r) {
        if (size > STORAGE_LIMIT) {
            System.out.println("База переполнена");
        } else if (storage[size] != null) {
            System.out.println("Ячейка занята");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("Не найден ваш " + uuid);
            return null;
        } else {
            return storage[findIndex(uuid)];
        }

    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("Не найден ваш " + uuid);
        } else {
            storage[findIndex(uuid)] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
