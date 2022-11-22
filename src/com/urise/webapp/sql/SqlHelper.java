package com.urise.webapp.sql;

import com.urise.webapp.LoggerConfig;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
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
            throw convertException(e);
        }
    }

    public void executeUpdateAndLogIfNothingUpdates(PreparedStatement ps, String uuid) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    public <T> T transactionExecute(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            LOGGER.info("get connection from connection factory");
            try {
                connection.setAutoCommit(false);
                T result = executor.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private StorageException convertException(SQLException e) {
        if (e.getSQLState().equals(SqlHelper.SQLSTATE_DUPLICATE_KEY)) {
            return new ExistStorageException(e);
        } else {
            return new StorageException(e);
        }
    }
}
