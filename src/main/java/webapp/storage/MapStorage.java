package main.java.webapp.storage;

import main.java.webapp.model.Resume;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {

    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume resume, String searchKey) {
        storage.put(searchKey, resume);
    }

    @Override
    protected Resume doGet(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected boolean isExisting(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    protected void doSave(Resume resume, String searchKey) {
        storage.put(searchKey, resume);
    }

    @Override
    protected void doDelete(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
