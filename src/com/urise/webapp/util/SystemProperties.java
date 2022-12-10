package com.urise.webapp.util;

import java.io.File;

public abstract class SystemProperties {
    public static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()) {
            throw new IllegalArgumentException(homeDir + " is not directory!");
        }
        return homeDir;
    }
}
