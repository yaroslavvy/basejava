package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final File propertyFile = new File("./config/resumes.properties");
    private final Properties properties = new Properties();
    private final Storage storage;
    private final String storageDir;

    private Config() {
        try (InputStream is = new FileInputStream(propertyFile.getAbsolutePath())) {
            properties.load(is);
            storage = new SqlStorage(properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
            storageDir = properties.getProperty("storage.dir");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + propertyFile.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public Storage getStorage() {
        return storage;
    }

    public String getStorageDir() {
        return storageDir;
    }
}
