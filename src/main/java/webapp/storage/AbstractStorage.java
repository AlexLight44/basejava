package main.java.webapp.storage;

import main.java.webapp.exeption.ExistStorageException;
import main.java.webapp.exeption.NotExistStorageException;
import main.java.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {
    protected abstract SK getSearchKey(String uuid);

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract boolean isExisting(SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);
    protected abstract List<Resume> doGetAll();


    public void update(Resume r) {
        SK res = getFindIndex(r.getUuid());
        doUpdate(r, res);
    }

    public void save(Resume r) {
        SK res = getNotFindIndex(r.getUuid());
        doSave(r, res);
    }

    public Resume get(String uuid) {
        SK res = getFindIndex(uuid);
        return doGet(res);
    }

    public void delete(String uuid) {
        SK res = getFindIndex(uuid);
        doDelete(res);
    }

    private SK getFindIndex(String uuid) {
        SK index = getSearchKey(uuid);
        if (!isExisting(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private SK getNotFindIndex(String uuid) {
        SK index = getSearchKey(uuid);
        if (isExisting(index)) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = doGetAll();
        list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return list;
    }

}
