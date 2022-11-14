package com.urise.webapp.sql;

import com.urise.webapp.LoggerConfig;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static final String SQLSTATE_DUPLICATE_KEY = "23505";
    private static final Logger LOGGER = LoggerConfig.getLogger(SqlHelper.class);
    private final ConnectionFactory connectionFactory;

    public SqlHelper(String url, String user, String password) {
        connectionFactory = () -> DriverManager.getConnection(url, user, password);
        LOGGER.info("connection factory was initialize with " + url);
    }

    @FunctionalInterface
    public interface FunctionWithSqlException<T, R> {
        R apply(T t) throws SQLException;
    }

    public <R> R executeStatement(String sqlStatement, FunctionWithSqlException<PreparedStatement, R> function) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
            LOGGER.info("get connection from connection factory");
            return function.apply(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals(SqlHelper.SQLSTATE_DUPLICATE_KEY)) {
                throw new ExistStorageException(e);
            } else {
                throw new StorageException(e);
            }
        }
    }

    @FunctionalInterface
    public interface ExceptionSupplier {
        StorageException get();
    }

    public void executeUpdateAndLogIfNothingUpdates(PreparedStatement ps, ExceptionSupplier exceptionSupplier) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw exceptionSupplier.get();
        }
    }
}
