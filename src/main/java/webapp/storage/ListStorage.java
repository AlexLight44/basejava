package main.java.webapp.storage;

import main.java.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    List<Resume> list = new ArrayList<>();

    @Override
    protected boolean isFind(Object index) {
        return index != null;
    }

    @Override
    protected void doSave(Resume r, Integer index) {
        list.add(r);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    protected void doDelete(Integer index) {
        list.remove(index.intValue());
    }

    @Override
    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    protected Integer getIndex(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected void doUpdate(Resume r, Integer index) {
        list.set(index, r);
    }

    @Override
    protected Resume doGet(Integer index) {
        return list.get(index);
    }
}
