package ru.shoma.webapp.storage.serializers;

import ru.shoma.webapp.exception.StorageException;
import ru.shoma.webapp.model.Resume;

import java.io.*;

public class ObjectStreamSerializer implements SerializeStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        }
    }
    @Override
    public Resume doRead (InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException(null, "Ошибка чтения файла");
        }
    }
}
