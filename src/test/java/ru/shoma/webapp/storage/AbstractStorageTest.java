package ru.shoma.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.shoma.webapp.Config;
import ru.shoma.webapp.exception.ExistStorageException;
import ru.shoma.webapp.exception.NotExistStorageException;
import ru.shoma.webapp.model.ContactType;
import ru.shoma.webapp.model.Resume;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.shoma.webapp.TestData.*;


public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();
    protected Storage storage;


    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(storage.size(), 0);
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_2, UUID_2);
        R2.addContact(ContactType.EMAIL, "soufee1@yandex.ru");
        R2.addContact(ContactType.CELLPHONE, "+79896566565");
        storage.update(resume);
        System.out.println(resume);
        System.out.println(storage.get(UUID_2));
        Assert.assertTrue(resume.equals(storage.get(UUID_2)));
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume("Ashamaz", "Ashamaz"));
        assertEquals(storage.size(), 4);
        Resume r = storage.get("Ashamaz");
        Assert.assertNotNull(r);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(new Resume("Ashamaz", "Ashamaz"));
        storage.save(new Resume("Ashamaz", "Ashamaz"));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(new Resume("Ashamaz").getUuid());
    }

    @Test
    public void size() throws Exception {
        assertEquals(3, storage.size());
    }

    @Test
    public void get() throws Exception {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertEquals(storage.size(), 2);
        storage.get(UUID_1);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        List<Resume> arr = new ArrayList<>();
        arr.add(R1);
        arr.add(R2);
        arr.add(R3);
        assertEquals(arr, list);
    }
}