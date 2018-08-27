package ru.shoma.webapp.util;

import org.junit.Assert;
import org.junit.Test;
import ru.shoma.webapp.model.*;

import java.time.Month;

import static org.junit.Assert.*;
import static ru.shoma.webapp.TestData.R1;
import static ru.shoma.webapp.TestData.R2;

/**
 * Created by Shoma on 17.07.2018.
 */
public class JsonParserTest {
    @Test
    public void testResume() throws Exception {
        String json = JsonParser.write(R1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(R1, resume);
    }

    @Test
    public void testSection() throws Exception {
        Section section1 = new TextSection("Z1");
        Section section2 = R2.getSection(SectionType.PERSONAL);
        Section section3 = R2.getSection(SectionType.QUALIFICATIONS);
        Section section4 = new OrgSection(
                new Organization("Мед академия, Владикавказ", null, new Organization.Position(2013, Month.JULY, 2014, Month.AUGUST, "Деканат", null)),
                new Organization("газета Терек", null, new Organization.Position(2017, Month.MARCH, 2017, Month.MAY, "Корреспондент", null)));

        Assert.assertEquals(section1, getDeserializedObject(section1, Section.class));
        Assert.assertEquals(section2, getDeserializedObject(section2, Section.class));
        Assert.assertEquals(section3, getDeserializedObject(section3, Section.class));
        Assert.assertEquals(section4, getDeserializedObject(section4, Section.class));

    }

    private <T> T getDeserializedObject(T o, Class<T> clazz) {
        String json = JsonParser.write(o, clazz);
        System.out.println(json);
        return JsonParser.read(json, clazz);
    }

}