package com.urise.webapp.sql;

import com.urise.webapp.Config;
import com.urise.webapp.exception.StorageException;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static final String SQLSTATE_DUPLICATE_KEY = "23505";
    private static final Logger LOGGER;
    private static final ConnectionFactory connectionFactory;

    static {
        Config config = Config.get();
        Configurator.initialize(null, config.getStorage().getProperty("log.config"));
        LOGGER = LoggerFactory.getLogger(SqlHelper.class);
        connectionFactory = () -> DriverManager.getConnection(
                config.getStorage().getProperty("db.url"),
                config.getStorage().getProperty("db.user"),
                config.getStorage().getProperty("db.password"));
        LOGGER.info("connection factory was initialize with " + config.getStorage().getProperty("db.url"));
    }

    @FunctionalInterface
    public interface FunctionWithSqlException<T, R> {
        R apply(T t) throws SQLException;
    }

    public static <R> R executeStatement(String sqlStatement, FunctionWithSqlException<PreparedStatement, R> function) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
            LOGGER.info("get connection from connection factory");
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
