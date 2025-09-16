package webapp.storage;

import webapp.exeption.ExistStorageException;
import webapp.exeption.NotExistStorageException;
import webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract SK getSearchKey(String uuid);

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract boolean isExisting(SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doGetAll();


    public void update(Resume r) {
        LOG.info("Update " + r);
        SK res = getFindIndex(r.getUuid());
        doUpdate(r, res);
    }

    public void save(Resume r) {
        LOG.info("\u001B[32m" +"Save " + r);  //TODO move it to the config file when SLF4J is added
        SK res = getNotFindIndex(r.getUuid());
        doSave(r, res);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK res = getFindIndex(uuid);
        return doGet(res);
    }

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK res = getFindIndex(uuid);
        doDelete(res);
    }

    private SK getFindIndex(String uuid) {
        SK index = getSearchKey(uuid);
        if (!isExisting(index)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private SK getNotFindIndex(String uuid) {
        SK index = getSearchKey(uuid);
        if (isExisting(index)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = doGetAll();
        list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return list;
    }

}
