package ru.shoma.webapp.storage;

import ru.shoma.webapp.exception.StorageException;
import ru.shoma.webapp.model.Resume;

import java.util.*;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doUpdate(Resume r, Integer searchKey) {
        storage[searchKey] = r;
    }

    @Override
    protected void doDelete(Integer searchKey) {
        fillDeletedElement(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Resume r, Integer searchKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException(r.getUuid(), "Хранилище переполнено");
        } else {
            insertElement(r, searchKey);
            size++;
        }
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[ searchKey];
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return  searchKey >= 0;
    }

    public int size() {
        return size;
    }

    protected List<Resume> doCopyAll() {
        Resume[] r = new Resume[size];
        System.arraycopy(storage, 0, r, 0, size);
        return Arrays.asList(r);
    }


    protected abstract Integer getSearchKey(String uuid);

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertElement(Resume r, int index);
}
