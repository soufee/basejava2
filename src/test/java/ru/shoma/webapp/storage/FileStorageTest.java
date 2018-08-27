package ru.shoma.webapp.storage;

import ru.shoma.webapp.storage.serializers.ObjectStreamSerializer;


public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}