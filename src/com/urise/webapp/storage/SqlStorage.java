package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;
    private static final Logger LOGGER;

    static {
        Configurator.initialize(null, Config.get().getLogConfig());
        LOGGER = LoggerFactory.getLogger(AbstractStorage.class);
    }

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        LOGGER.info("connection factory was initialize with " + dbUrl);
    }

    @Override
    public void clear() {
        executeStatement("DELETE FROM resumes", (ps) -> {
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        executeStatement("INSERT INTO resumes (resume_id, full_name) VALUES (?, ?)", (ps) -> {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return executeStatement("SELECT * FROM resumes WHERE resume_id = ?", (ps) -> {
            ps.setString(1, uuid);
            LOGGER.info("trying to execute sql statement: " + ps);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                NotExistStorageException e = new NotExistStorageException(uuid);
                LOGGER.error(e.getMessage(), e);// TODO Del me. I know it bad practice. ↓↓↓
                // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
                throw e;
            }
            Resume resume = new Resume(resultSet.getString("resume_id").trim(), resultSet.getString("full_name"));
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        executeStatement("DELETE FROM resumes WHERE resume_id = ?", (ps) -> {
            ps.setString(1, uuid);
            LOGGER.info("trying to execute sql statement: " + ps);
            executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(uuid));
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return executeStatement("SELECT * FROM resumes ORDER BY full_name, resume_id", (ps) -> {
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
        return executeStatement("SELECT COUNT(*) FROM resumes", (ps) -> {
            LOGGER.info("trying to execute sql statement: " + ps);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return (int) resultSet.getLong("count");
        });
    }

    @Override
    public void update(Resume resume) {
        executeStatement("UPDATE resumes SET full_name = ? WHERE resume_id = ?", (ps) -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            LOGGER.info("trying to execute sql statement: " + ps);
            executeUpdateAndLogIfNothingUpdates(ps, () -> new NotExistStorageException(resume.getUuid()));
            return null;
        });
    }

    @FunctionalInterface
    public interface FunctionWithSqlException<T, R> {
        R apply(T t) throws SQLException;
    }

    private <R> R executeStatement(String sqlStatement, FunctionWithSqlException<PreparedStatement, R> function) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
            LOGGER.info("get connection from connection factory");
            return function.apply(ps);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);// TODO Del me. I know it bad practice. ↓↓↓
            // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface ExceptionSupplier {
        StorageException get();
    }

    private void executeUpdateAndLogIfNothingUpdates(PreparedStatement ps, ExceptionSupplier exceptionSupplier) throws SQLException {
        if (ps.executeUpdate() == 0) {
            StorageException storageException = exceptionSupplier.get();
            LOGGER.error(storageException.getMessage(), storageException);// TODO Del me. I know it bad practice. ↓↓↓
            // Rule - catch and log or catch and throw in another exception. Never both. Only for demonstration of logger work
            throw storageException;
        }
    }
}
