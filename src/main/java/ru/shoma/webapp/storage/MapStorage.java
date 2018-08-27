package ru.shoma.webapp.storage;

import ru.shoma.webapp.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage <String>{

    private Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, String searchKey) {
        storage.replace(searchKey, r);
    }

    @Override
    protected void doDelete(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected void doSave(Resume r, String searchKey) {
        storage.put(searchKey, r);
    }

    @Override
    protected Resume doGet(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected boolean isExist(String searchKey) {

        return storage.containsKey(getSearchKey(searchKey));
    }
}
