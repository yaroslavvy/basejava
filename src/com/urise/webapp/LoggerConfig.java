package com.urise.webapp;

import com.urise.webapp.util.SystemProperties;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class LoggerConfig {
    static {
        Configurator.initialize(null, new File(SystemProperties.getHomeDir(), "config/log4j2.xml").getAbsolutePath());
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
