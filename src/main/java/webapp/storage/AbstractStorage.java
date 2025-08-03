package main.java.webapp.storage;

import main.java.webapp.exeption.ExistStorageException;
import main.java.webapp.exeption.NotExistStorageException;
import main.java.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected abstract Integer getIndex(String uuid);

    protected abstract void doUpdate(Resume r, Integer index);

    protected abstract Resume doGet(Integer index);

    protected abstract boolean isFind(Object index);

    protected abstract void doSave(Resume r, Integer index);

    protected abstract void doDelete(Integer index);


    public void update(Resume r) {
        Integer res = getFindIndex(r.getUuid());
        doUpdate(r, res);
    }

    public void save(Resume r) {
        Integer res = getNotFindIndex(r.getUuid());
        doSave(r, res);
    }

    public Resume get(String uuid) {
        Integer res = getFindIndex(uuid);
        return doGet(res);
    }

    public void delete(String uuid) {
        Integer res = getFindIndex(uuid);
        doDelete(res);
    }

    private Integer getFindIndex(String uuid) {
        Integer index = getIndex(uuid);
        if (!isFind(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private Integer getNotFindIndex(String uuid) {
        Integer index = getIndex(uuid);
        if (isFind(index)) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }
}
