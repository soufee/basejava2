package ru.shoma.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUid = 1L;

    private String uuid;
    private String fullName;
    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
    public static final Resume EMPTY = new Resume();

    static {
        EMPTY.addSectionItem(SectionType.OBJECTIVE, TextSection.EMPTY);
        EMPTY.addSectionItem(SectionType.PERSONAL, TextSection.EMPTY);
        EMPTY.addSectionItem(SectionType.ACHIEVEMENT, ListSection.EMPTY);
        EMPTY.addSectionItem(SectionType.QUALIFICATIONS, ListSection.EMPTY);
        EMPTY.addSectionItem(SectionType.EXPERIENCE, new OrgSection(Organization.EMPTY));
        EMPTY.addSectionItem(SectionType.EDUCATION, new OrgSection(Organization.EMPTY));
    }
    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume() {
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(fullName, "fullName must not be null or empty");
        Objects.requireNonNull(uuid, "uuid must not be null or empty");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void addContact(ContactType ct, String item) {
        contacts.put(ct, item);
    }

    public void addSectionItem(SectionType ct, Section s) {
        sections.put(ct, s);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        StringBuilder summary = new StringBuilder("Резюме № " + uuid + "\n");
        summary.append("ФИО: ").append(fullName).append("\n").append("Контактная информация\n");
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            summary.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            summary.append(entry.getKey()).append(" : \n").append(entry.getValue()).append("\n");
        }

        return summary.toString();
    }

    @Override
    public int compareTo(Resume o) {
        int nameCmp = this.fullName.compareTo(o.getFullName());
        return nameCmp == 0 ? this.uuid.compareTo(o.getUuid()) : nameCmp;
    }

}
