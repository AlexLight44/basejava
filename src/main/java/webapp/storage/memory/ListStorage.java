package webapp.storage.memory;


import webapp.model.Resume;
import webapp.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    List<Resume> list = new ArrayList<>();

    @Override
    protected boolean isExisting(Integer index) {
        return index != null;
    }

    @Override
    protected void doSave(Resume r, Integer searchKey) {
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
    protected void doDelete(Integer searchKey) {
        list.remove(searchKey.intValue());
    }

    @Override
    protected List<Resume> doGetAll() {
        return list;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
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
    protected Resume doGet(Integer searchKey) {
        return list.get(searchKey);
    }
}
