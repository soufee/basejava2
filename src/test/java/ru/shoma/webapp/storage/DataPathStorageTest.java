package ru.shoma.webapp.storage;

import ru.shoma.webapp.storage.serializers.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
            }
}
