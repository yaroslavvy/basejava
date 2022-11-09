package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private static final Logger LOGGER;

    static {
        Configurator.initialize(null, Config.get().getStorage().getProperty("log.config"));
        LOGGER = LoggerFactory.getLogger(SqlStorage.class);
    }

    public SqlStorage() {
    }

    @Override
    public void clear() {
        SqlHelper.executeStatement("DELETE FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ps.execute();
                    return null;
                });
    }

    @Override
    public void save(Resume resume) {
        SqlHelper.executeStatement("INSERT INTO resumes (resume_id, full_name) VALUES (?, ?)",
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
        return SqlHelper.executeStatement("SELECT * FROM resumes WHERE resume_id = ?",
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
        SqlHelper.executeStatement("DELETE FROM resumes WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    SqlHelper.executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(uuid));
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.executeStatement("SELECT * FROM resumes ORDER BY full_name, resume_id",
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
        return SqlHelper.executeStatement("SELECT COUNT(*) FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    resultSet.next();
                    return (int) resultSet.getLong("count");
                });
    }

    @Override
    public void update(Resume resume) {
        SqlHelper.executeStatement("UPDATE resumes SET full_name = ? WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, resume.getFullName());
                    ps.setString(2, resume.getUuid());
                    LOGGER.info("trying to execute sql statement: " + ps);
                    SqlHelper.executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(resume.getUuid()));
                    return null;
                });
    }
}
