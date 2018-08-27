package ru.shoma.webapp.storage;

import ru.shoma.webapp.exception.NotExistStorageException;
import ru.shoma.webapp.exception.StorageException;
import ru.shoma.webapp.model.*;
import ru.shoma.webapp.sql.SqlHelper;
import ru.shoma.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {

        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(conn -> {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                statement.setString(1, r.getFullName());
                statement.setString(2, r.getUuid());
                if (statement.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            insertContacts(conn, r);
            deleteSections(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(conn -> {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                statement.setString(1, r.getUuid());
                statement.setString(2, r.getFullName());
                statement.execute();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.execute(" SELECT * FROM resume r" +
                " LEFT JOIN contact c " +
                " ON r.uuid = c.resume_uuid " +
                " LEFT JOIN section s ON r.uuid = s.resume_uuid " +
                " WHERE r.uuid = ?", statement -> {
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContact(rs, r);
                addSection(rs, r);
            } while (rs.next());
            return r;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> resumes = new LinkedHashMap<>();
        return helper.transactionalExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Resume r = resumes.get(resultSet.getString("resume_uuid"));
                    addContact(resultSet, r);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Resume r = resumes.get(resultSet.getString("resume_uuid"));
                    addSection(resultSet, r);
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public void delete(String uuid) {
        helper.execute("DELETE FROM resume WHERE uuid = ?", statement -> {
            statement.setString(1, uuid);
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public int size() {
        return helper.execute("SELECT count(*) FROM resume", statement -> {
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        });
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE  FROM contact WHERE resume_uuid=?");
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE  FROM section WHERE resume_uuid=?");
    }

    private void deleteAttributes(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                statement.setString(1, r.getUuid());
                statement.setString(2, e.getKey().name());
                statement.setString(3, e.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO section (resume_uuid, type_s, value_s) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                statement.setString(1, r.getUuid());
                statement.setString(2, e.getKey().name());
                switch (e.getKey().name()) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        TextSection sectionText = (TextSection) e.getValue();
                        statement.setString(3, sectionText.getContent());
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        ListSection sectionList = (ListSection) e.getValue();
                        String sb = String.join("\n", sectionList.getAll());
                        statement.setString(3, sb.trim());
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION": {
                        Section section = (Section) e.getValue();
                        statement.setString(3, JsonParser.write(section, Section.class));
                    }
                    break;
                    default:
                        throw new StorageException("Тип секции не распознан");
                }
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }


    private void addContact(ResultSet results, Resume resume) throws SQLException {
        String value = results.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(results.getString("type")), value);
        }
    }

    private void addSection(ResultSet results, Resume resume) throws SQLException {
        String sectionType = results.getString("type_s");
        if (sectionType != null) {
            String value = results.getString("value_s");
            switch (sectionType) {
                case "PERSONAL":
                case "OBJECTIVE":
                    if (value != null) {
                        TextSection section = new TextSection(value);
                        resume.addSectionItem(SectionType.valueOf(sectionType), section);
                    }
                    break;
                case "ACHIEVEMENT":
                case "QUALIFICATIONS":
                    if (value != null) {
                        String[] list = value.split("\n");
                        List<String> sectionValues = new ArrayList<>(Arrays.asList(list));
                        ListSection listSection = new ListSection(sectionValues);
                        resume.addSectionItem(SectionType.valueOf(sectionType), listSection);
                    }
                    break;
                case "EXPERIENCE":
                case "EDUCATION": {
                    Section section = JsonParser.read(value, Section.class);
                    resume.addSectionItem(SectionType.valueOf(sectionType), section);
                }
                break;
                default:
                    throw new StorageException("Тип секции не распознан");
            }
        }
    }

}
