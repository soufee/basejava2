package ru.shoma.webapp.storage;

import ru.shoma.webapp.exception.ExistStorageException;
import ru.shoma.webapp.exception.NotExistStorageException;
import ru.shoma.webapp.model.Resume;

import java.util.*;
import java.util.logging.Logger;


public abstract class AbstractStorage<K> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract K getSearchKey(String uuid);

    protected abstract void doUpdate(Resume r, K searchKey);

    protected abstract void doDelete(K searchKey);

    protected abstract void doSave(Resume r, K searchKey);

    protected abstract Resume doGet(K searchKey);

    protected abstract boolean isExist(K searchKey);

    protected abstract List<Resume> doCopyAll();

    public void update(Resume r) {
        LOG.info("Update : " + r.toString());
        K searchKey = getExistedSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    public void save(Resume r) {
        LOG.info("Save : " + r.toString());
        K searchKey = getNotExistedSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    public void delete(String uuid) {
        LOG.info("Delete : " + uuid);
        K searchKey = getExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        K searchKey = getExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    private K getExistedSearchKey(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("GetExistedKey : " + uuid);
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private K getNotExistedSearchKey(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.info("GetNotExistedKey : " + uuid);
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        List<Resume> list = doCopyAll();
        Collections.sort(list);
        return list;
    }

}
