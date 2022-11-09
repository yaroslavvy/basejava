package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    public static final String SQLSTATE_DUPLICATE_KEY = "23505";

    @FunctionalInterface
    public interface FunctionWithSqlException<T, R> {
        R apply(T t) throws SQLException;
    }

    public static <R> R executeStatement(String sqlStatement, ConnectionFactory connectionFactory,
                                         Logger logger, FunctionWithSqlException<PreparedStatement, R> function) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
            logger.info("get connection from connection factory");
            return function.apply(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface ExceptionSupplier {
        StorageException get();
    }

    public static void executeUpdateAndLogIfNothingUpdates(PreparedStatement ps, ExceptionSupplier exceptionSupplier) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw exceptionSupplier.get();
        }
    }
}
