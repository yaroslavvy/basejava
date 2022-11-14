package com.urise.webapp.storage;

import com.urise.webapp.LoggerConfig;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.executeStatement("INSERT INTO resumes (resume_id, full_name) VALUES (?, ?)",
                (ps) -> {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, resume.getFullName());
                    LOGGER.info("trying to execute sql statement: " + ps);
                    try {
                        ps.execute();
                    } catch (SQLException e) {
                        if (e.getSQLState().equals(SqlHelper.SQLSTATE_DUPLICATE_KEY)) {
                            throw new ExistStorageException(e);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement("SELECT * FROM resumes WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(resultSet.getString("resume_id").trim(), resultSet.getString("full_name"));
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement("DELETE FROM resumes WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(uuid));
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeStatement("SELECT * FROM resumes ORDER BY full_name, resume_id",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (resultSet.next()) {
                        Resume resume = new Resume(resultSet.getString("resume_id").trim(), resultSet.getString("full_name"));
                        resumes.add(resume);
                    }
                    return resumes;
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
        sqlHelper.executeStatement("UPDATE resumes SET full_name = ? WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, resume.getFullName());
                    ps.setString(2, resume.getUuid());
                    LOGGER.info("trying to execute sql statement: " + ps);
                    sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(resume.getUuid()));
                    return null;
                });
    }
}
