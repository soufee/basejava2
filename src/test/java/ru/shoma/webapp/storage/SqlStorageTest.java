package ru.shoma.webapp.storage;

import ru.shoma.webapp.Config;

/**
 * Created by Shoma on 18.05.2018.
 */
public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.getInstance().getStorage());
    }
}