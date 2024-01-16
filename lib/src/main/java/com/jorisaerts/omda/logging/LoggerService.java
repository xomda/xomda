package com.jorisaerts.omda.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class LoggerService {

    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

    public static void init() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        config.getLoggerConfig("").setLevel(DEFAULT_LOG_LEVEL);
        ctx.updateLoggers();
    }


}
