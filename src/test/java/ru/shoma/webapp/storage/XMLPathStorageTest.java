package ru.shoma.webapp.storage;

import ru.shoma.webapp.storage.serializers.XmlStreamSerializer;


public class XMLPathStorageTest extends AbstractStorageTest {
    public XMLPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerializer()));
            }
}
