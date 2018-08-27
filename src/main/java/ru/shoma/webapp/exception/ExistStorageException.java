package ru.shoma.webapp.exception;

/**
 * Created by Shoma on 09.02.2018.
 */
public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super(uuid, "Запись уже существует");
    }
}
