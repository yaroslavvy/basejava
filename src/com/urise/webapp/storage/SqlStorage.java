package com.urise.webapp.storage;

import com.urise.webapp.LoggerConfig;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.urise.webapp.storage.AbstractStorage.RESUME_COMPARATOR_FULL_NAME_THEN_UUID;

public class SqlStorage implements Storage {
    private static final Logger LOGGER = LoggerConfig.getLogger(SqlStorage.class);
    private final SqlHelper sqlHelper;

    public SqlStorage(String url, String user, String password) {
        sqlHelper = new SqlHelper(url, user, password);
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ps.execute();
                    return null;
                });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resumes (resume_id, full_name) VALUES (?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                LOGGER.info("trying to execute sql statement: " + ps);
                ps.execute();
            }
            insertContacts(resume, connection);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement("" +
                        "SELECT * FROM resumes r " +
                        "LEFT JOIN contacts c USING (resume_id) " +
                        "WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(resultSet.getString("resume_id"), resultSet.getString("full_name"));
                    do {
                        ContactType contactType = ContactType.values()[resultSet.getInt("contact_type_id")];
                        String value = resultSet.getString("value");
                        if (value != null) {
                            resume.addContact(contactType, value);
                        }
                    } while (resultSet.next());
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement("DELETE FROM resumes WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, uuid);
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeStatement("SELECT * FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    Map<String, Resume> resumeMap = new HashMap<>();
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        String resume_id = resultSet.getString("resume_id");
                        resumeMap.put(resume_id, new Resume(resume_id, resultSet.getString("full_name")));
                    }
                    return sqlHelper.executeStatement("SELECT * FROM contacts",
                            (ps2) -> {
                                LOGGER.info("trying to execute sql statement: " + ps2);
                                ResultSet resultSet2 = ps2.executeQuery();
                                while (resultSet2.next()) {
                                    String resume_id = resultSet2.getString("resume_id");
                                    ContactType contactType = ContactType.values()[resultSet2.getInt("contact_type_id")];
                                    String value = resultSet2.getString("value");
                                    resumeMap.get(resume_id).addContact(contactType, value);
                                }
                                return resumeMap.values().stream().sorted(RESUME_COMPARATOR_FULL_NAME_THEN_UUID).collect(Collectors.toList());
                            });
                });
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement("SELECT COUNT(*) FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    resultSet.next();
                    return (int) resultSet.getLong("count");
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resumes SET full_name = ? WHERE resume_id = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                LOGGER.info("trying to execute sql statement: " + ps);
                sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, resume.getUuid());
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contacts WHERE resume_id = ?")) {
                ps.setString(1, resume.getUuid());
                LOGGER.info("trying to execute sql statement: " + ps);
                ps.execute();
            }
            insertContacts(resume, connection);
            return null;
        });
    }

    private void insertContacts(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contacts (resume_id, contact_type_id, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setInt(2, entry.getKey().ordinal());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.executeBatch();
        }
    }
}
