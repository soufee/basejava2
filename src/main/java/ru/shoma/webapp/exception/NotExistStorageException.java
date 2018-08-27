package ru.shoma.webapp.exception;

/**
 * Created by Shoma on 09.02.2018.
 */
public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super(uuid, "Запись не найдена");
    }
}
