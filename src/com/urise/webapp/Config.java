package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final File PROPERTY_FILE = new File("./config/resumes.properties");
    private final Properties properties = new Properties();
    private final String STORAGE_DIR;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String LOG_CONFIG;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTY_FILE.getAbsolutePath())) {
            properties.load(is);
            STORAGE_DIR = properties.getProperty("storage.dir");
            DB_URL = properties.getProperty("db.url");
            DB_USER = properties.getProperty("db.user");
            DB_PASSWORD = properties.getProperty("db.password");
            LOG_CONFIG = properties.getProperty("log.config");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPERTY_FILE.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public String getStorageDir() {
        return STORAGE_DIR;
    }

    public String getDbUrl() {
        return DB_URL;
    }

    public String getDbUser() {
        return DB_USER;
    }

    public String getDbPassword() {
        return DB_PASSWORD;
    }

    public String getLogConfig() {
        return LOG_CONFIG;
    }
}
