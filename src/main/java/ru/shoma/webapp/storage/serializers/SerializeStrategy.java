package ru.shoma.webapp.storage.serializers;

import ru.shoma.webapp.model.Resume;

import java.io.*;

/**
 * Created by Shoma on 14.04.2018.
 */
public interface SerializeStrategy {
    void doWrite(Resume r, OutputStream os) throws IOException;
    Resume doRead(InputStream is) throws IOException;
}
