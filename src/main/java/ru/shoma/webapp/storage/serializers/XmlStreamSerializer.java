package ru.shoma.webapp.storage.serializers;

import ru.shoma.webapp.model.*;

import ru.shoma.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class XmlStreamSerializer implements SerializeStrategy {
    private XmlParser parser;

    public XmlStreamSerializer() {
        this.parser = new XmlParser(
                Resume.class,
                Organization.class,
                Link.class,
                OrgSection.class,
                TextSection.class,
                ListSection.class,
                Organization.Position.class
        );
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
       try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
        parser.marshall(r, w);
       }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return parser.unmarshall(r);
        }
    }
}
