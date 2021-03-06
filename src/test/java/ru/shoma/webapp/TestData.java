package ru.shoma.webapp;

import ru.shoma.webapp.model.*;

import java.time.Month;
import java.util.UUID;


public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static Resume R1;
    public static Resume R2;
    public static Resume R3;

    static {
        R1 = new Resume(UUID_1, "Name 1");
        R2 = new Resume(UUID_2, "Name 2");
        R3 = new Resume(UUID_3, "Name 3");

        R1.addContact(ContactType.EMAIL, "soufee@mail.ru");
        R1.addContact(ContactType.CELLPHONE, "+79604268452");

        R1.addSectionItem(SectionType.OBJECTIVE, new TextSection("Java Developer"));
        R1.addSectionItem(SectionType.PERSONAL, new TextSection("Личные качества очень хорошие, мамой клянус!"));
        R1.addSectionItem(SectionType.ACHIEVEMENT, new ListSection("OCA сертификат", "Сертификат Университета Иннополис"));
        R1.addSectionItem(SectionType.QUALIFICATIONS, new ListSection("Java EE", "Pega BPM", "Spring MVC", "Spring IoC", "Spring security", "Hibernate", "SQL"));
        R1.addSectionItem(SectionType.EXPERIENCE,
                new OrgSection(
                        new Organization("Ай-Теко", "http:\\\\i-teco.ru", new Organization.Position(2017, Month.JULY, "Старший разработчик", "Java & Pega developer")),
                        new Organization("Новые информационные технологии", "", new Organization.Position(2007, Month.JULY, 2009, Month.SEPTEMBER, "Программист", "Delphi"))));
        R1.addSectionItem(SectionType.EDUCATION,
                new OrgSection(new Organization("КБЭПЛ", "",
                        new Organization.Position(2000, Month.SEPTEMBER, 2003, Month.JUNE, "Student", "")),
                        new Organization("КБИБ", "",
                                new Organization.Position(2003, Month.SEPTEMBER, 2008, Month.JULY, "Student", ""))));

        R2.addContact(ContactType.EMAIL, "zarina@mail.ru");
        R2.addContact(ContactType.CELLPHONE, "+79778339880");

        R2.addSectionItem(SectionType.OBJECTIVE, new TextSection("Корреспондент"));
        R2.addSectionItem(SectionType.PERSONAL, new TextSection("Личные данные"));
        R2.addSectionItem(SectionType.ACHIEVEMENT, new ListSection("Достиженние 1", "Достижение 2", "Достижение 3"));
        R2.addSectionItem(SectionType.QUALIFICATIONS, new ListSection("Умение 1", "Умение 2", "Умение 3"));
        R2.addSectionItem(SectionType.EXPERIENCE,
                new OrgSection(
                        new Organization("Мед академия, Владикавказ", null, new Organization.Position(2013, Month.JULY, 2014, Month.AUGUST, "Деканат", null)),
                        new Organization("газета Терек", null, new Organization.Position(2017, Month.MARCH, 2017, Month.MAY, "Корреспондент", null))));

        R2.addSectionItem(SectionType.EDUCATION,
                new OrgSection(new Organization("Московский государственный педагогический институт", null,
                        new Organization.Position(2004, Month.SEPTEMBER, 2009, Month.JUNE, "Student", null))));

    }
}
