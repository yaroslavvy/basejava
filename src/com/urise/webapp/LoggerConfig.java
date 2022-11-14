package com.urise.webapp;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggerConfig {
    static {
        Configurator.initialize(null, "config/log4j2.xml");
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
