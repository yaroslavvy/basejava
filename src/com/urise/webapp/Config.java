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
    private static final Storage storage = new SqlStorage();

    private Config() {
        try (InputStream is = new FileInputStream(propertyFile.getAbsolutePath())) {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + propertyFile.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public Properties getProperties() {
        return properties;
    }

    public Storage getStorage() {
        return storage;
    }
}
