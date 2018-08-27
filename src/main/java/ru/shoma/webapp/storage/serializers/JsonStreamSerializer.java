package ru.shoma.webapp.storage.serializers;

import ru.shoma.webapp.model.Resume;
import ru.shoma.webapp.util.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamSerializer implements SerializeStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            JsonParser.write(r, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return JsonParser.read(r, Resume.class);
        }
    }
}
