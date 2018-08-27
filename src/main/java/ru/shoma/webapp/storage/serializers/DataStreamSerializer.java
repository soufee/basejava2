package ru.shoma.webapp.storage.serializers;

import ru.shoma.webapp.exception.StorageException;
import ru.shoma.webapp.model.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class DataStreamSerializer implements SerializeStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            //пишем контакты
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            // пишем секции
            writeCollection(dos, r.getSections().entrySet(), entry -> {
                SectionType type = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {

                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) section).getAll(), dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrgSection) section).getOrganizations(), organization -> {
                            dos.writeUTF(organization.getHomepage().getName());
                            dos.writeUTF(organization.getHomepage().getUrl());
                            writeCollection(dos, organization.getPositions(), position -> {
                                dos.writeUTF(position.getTitle());
                                writeLocalDate(dos, position.getStartDate());
                                writeLocalDate(dos, position.getEndDate());
                                dos.writeUTF(position.getDescription());
                            });
                        });
                        break;
                }
            });

//
//            Map<SectionType, Section> sections = r.getSections();
//            dos.writeInt(sections.size());
//            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
//                dos.writeUTF(entry.getKey().name());
//                Section s = entry.getValue();
//                String sectionType = s.getClass().getName();
//                dos.writeUTF(sectionType);
//                switch (sectionType) {
//                    case "ru.shoma.webapp.model.TextSection":
//                        TextSection textSection = (TextSection) s;
//                        dos.writeUTF(textSection.getContent());
//                        break;
//                    case "ru.shoma.webapp.model.ListSection":
//                        ListSection listSection = (ListSection) s;
//                        writeCollection(dos, listSection.getAll(), e -> dos.writeUTF(e));
////                          dos.writeInt(listSection.getAll().size());
//                        //                         listSection.getAll().forEach(st -> {
////                            try {
////                                dos.writeUTF(st);
////                            } catch (IOException e) {
////                                new StorageException("Ошибка записи",listSection.toString(), e);
////                            }
////                        });
//                        break;
//                    case "ru.shoma.webapp.model.OrgSection":
//                        OrgSection orgSection = (OrgSection) s;
//                        dos.writeInt(orgSection.getOrganizations().size());
//                        System.out.println(orgSection.getOrganizations().size());
//                        for (Organization o : orgSection.getOrganizations()) {
//                            Link link = o.getHomepage();
//                            String name = link.getName();
//                            dos.writeUTF(name);
//                            String url = link.getUrl();
//                            dos.writeUTF(url != null ? url : " ");
//                            dos.writeInt(o.getPositions().size());
//                            for (Organization.Position position : o.getPositions()) {
//                                dos.writeUTF(position.getTitle());
//                                String descr = position.getDescription();
//                                dos.writeUTF(descr != null ? descr : " ");
//                                dos.writeUTF(String.valueOf(position.getStartDate()));
//                                dos.writeUTF(String.valueOf(position.getEndDate()));
//                            }
//                        }
//                        break;
//                    default:
//                        throw new IOException("Неизвестная ошибка");
//                }
//            }
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate startDate) throws IOException {
        dos.writeInt(startDate.getYear());
        dos.writeInt(startDate.getMonth().getValue());
    }

    private interface ElementWriter<T> {
        void write(T t) throws IOException;
    }

    private interface ElementProcessor {
        void process() throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ElementWriter<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private void readItems(DataInputStream dis, ElementProcessor processor) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            processor.process();
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSectionItem(sectionType, readSection(dis, sectionType));
            });
//            int size = dis.readInt();
//            for (int i = 0; i < size; i++) {
//                ContactType type = ContactType.valueOf(dis.readUTF());
//                String contactValue = dis.readUTF();
//                resume.addContact(type, contactValue);
//            }

//            int sizeOfSections = dis.readInt();
//            Map<SectionType, Section> sections = new HashMap<>(sizeOfSections);
//
//            for (int i = 0; i < sizeOfSections; i++) {
//                String sectionName = dis.readUTF();
//                String sectionType = dis.readUTF();
//
//                switch (sectionType) {
//                    case "ru.shoma.webapp.model.TextSection":
//                        Section section = new TextSection(dis.readUTF());
//                        sections.put(SectionType.valueOf(sectionName), section);
//                        break;
//                    case "ru.shoma.webapp.model.ListSection":
//                        int sizeOfListSection = dis.readInt();
//                        List<String> list = new ArrayList<>(sizeOfListSection);
//                        for (int j = 0; j < sizeOfListSection; j++) {
//                            list.add(dis.readUTF());
//                        }
//                        Section section1 = new ListSection(list);
//                        sections.put(SectionType.valueOf(sectionName), section1);
//                        break;
//                    case "ru.shoma.webapp.model.OrgSection":
//                        int orgListSize = dis.readInt();
//                        List<Organization> organizations = new ArrayList<>(orgListSize);
//                        for (int j = 0; j < orgListSize; j++) {
//                            String linkName = dis.readUTF();
//                            String url = dis.readUTF();
//                            url = url.equals(" ") ? null : url;
//                            Link link = new Link(linkName, url);
//                            int positionsSize = dis.readInt();
//                            List<Organization.Position> positions = new ArrayList<>(positionsSize);
//                            for (int k = 0; k < positionsSize; k++) {
//                                String title = dis.readUTF();
//                                String description = dis.readUTF();
//                                description = description.equals(" ") ? null : description;
//                                String start = dis.readUTF();
//                                String end = dis.readUTF();
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                LocalDate startDate = LocalDate.parse(start, formatter);
//                                LocalDate endDate = LocalDate.parse(end, formatter);
//                                Organization.Position position = new Organization.Position(title, startDate, endDate, description);
//                                positions.add(position);
//                            }
//                            Organization organization = new Organization(link, positions);
//                            organizations.add(organization);
//                        }
//                        Section section2 = new OrgSection(organizations);
//                        sections.put(SectionType.valueOf(sectionName), section2);
//                        break;
//                    default:
//                        throw new IOException("Неизвестная ошибка");
//                }
//
//            }
//            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
//                resume.addSectionItem(entry.getKey(), entry.getValue());
//            }
            return resume;
//
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readList(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                return new OrgSection(readList(dis, () -> new Organization(new Link(dis.readUTF(), dis.readUTF()),
                        readList(dis, () -> new Organization.Position(
                                dis.readUTF(), readLocalDate(dis), readLocalDate(dis), dis.readUTF()
                        )))));
            default:
                throw new IllegalStateException();
        }

    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    private interface ElementReader<T> {
        T read() throws IOException;
    }

    private <T> List<T> readList(DataInputStream dis, ElementReader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }
}
